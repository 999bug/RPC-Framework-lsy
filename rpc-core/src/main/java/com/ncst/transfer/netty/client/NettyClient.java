package com.ncst.transfer.netty.client;

import com.ncst.balance.LoadBalance;
import com.ncst.balance.RandomLoadBalancer;
import com.ncst.common.RpcConfig;
import com.ncst.common.RpcError;
import com.ncst.common.SerializerEnum;
import com.ncst.discovery.NacosServiceDiscovery;
import com.ncst.discovery.ServiceDiscovery;
import com.ncst.entity.RpcRequest;
import com.ncst.entity.RpcResponse;
import com.ncst.exception.RpcException;
import com.ncst.factory.SingletonFactory;
import com.ncst.serializer.CommonSerializer;
import com.ncst.transfer.socket.RpcClient;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author Lsy
 * @date 2022/1/24
 */
public class NettyClient implements RpcClient {

    private static final Logger logger = LoggerFactory.getLogger(NettyClient.class);
    private static final EventLoopGroup group;
    private static final Bootstrap bootstrap;
    private final ServiceDiscovery serviceDiscovery;
    private final CommonSerializer serializer;
    private final UnprocessedRequests unprocessedRequests;

    static {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioSocketChannel.class);
    }

    public NettyClient() {
        this(RpcConfig.getSerializerEnum(), RpcConfig.getLoadBalance());
    }

    public NettyClient(LoadBalance loadBalance) {
        this(RpcConfig.getSerializerEnum(), loadBalance);
    }

    public NettyClient(SerializerEnum serializerEnum) {
        this(serializerEnum, RpcConfig.getLoadBalance());
    }

    public NettyClient(SerializerEnum serializerEnum, LoadBalance loadBalance) {
        this.serviceDiscovery = new NacosServiceDiscovery(loadBalance);
        this.serializer = CommonSerializer.getSerializer(serializerEnum);
        this.unprocessedRequests = SingletonFactory.getInstance(UnprocessedRequests.class);
    }

    @Override
    public Object sendRequest(RpcRequest rpcRequest) {
        if (serializer == null) {
            logger.error(RpcError.SERIALIZER_NOT_FOUND.getMessage());
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        CompletableFuture<RpcResponse> future = new CompletableFuture<>();
        InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcRequest.getInterfaceName());
        Channel channel = null;
        try {
            channel = ChannelProvider.get(inetSocketAddress, serializer);
        } catch (ExecutionException | InterruptedException e) {
            unprocessedRequests.remove(rpcRequest.getRequestId());
            logger.error(e.getMessage(), e);
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }

        if (channel != null) {
            if (!channel.isActive()) {
                group.shutdownGracefully();
                return null;
            }
            unprocessedRequests.put(rpcRequest.getRequestId(), future);
            channel.writeAndFlush(rpcRequest).addListener((ChannelFutureListener) a -> {
                if (a.isSuccess()) {
                    logger.info("client send msg {}", rpcRequest);
                }else {
                    a.channel().close();
                    future.completeExceptionally(a.cause());
                    logger.error("send msg error {}", a.cause().getMessage());
                }
            });
            return future;
        }
        return null;
    }
}
