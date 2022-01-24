package com.ncst.transfer.netty.client;

import com.ncst.common.RpcDecoder;
import com.ncst.common.RpcEnCoder;
import com.ncst.serializer.CommonSerializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 获取 channel 对象
 *
 * @author Lsy
 * @date 2022/1/24
 */
public class ChannelProvider {

    private static final Logger logger = LoggerFactory.getLogger(ChannelProvider.class);
    private static final EventLoopGroup eventLoopGroup;
    private static final Bootstrap bootstrap;
    private static final Map<String, Channel> channelMap;

    private ChannelProvider() {
    }

    static {
        channelMap = new ConcurrentHashMap<>();
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                // 连接超时时间
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                // 是否开启 TCP 底层心跳机制
                .option(ChannelOption.SO_KEEPALIVE, true)
                // 开启 Nagle 算法：尽可能的发送最大数据块，减少网络传输
                .option(ChannelOption.TCP_NODELAY, true);
    }

    public static Channel get(InetSocketAddress inetSocketAddress, CommonSerializer serializer) throws ExecutionException, InterruptedException {
        String key = inetSocketAddress.toString() + serializer.getSerialCode();
        if (channelMap.containsKey(key)) {
            Channel channel = channelMap.get(key);
            if (channel.isActive()) {
                return channel;
            } else {
                channelMap.remove(key);
            }
        }

        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) {
                socketChannel.pipeline().addLast(new RpcEnCoder(serializer))
                        .addLast(new IdleStateHandler(0, 5, 0, TimeUnit.SECONDS))
                        .addLast(new RpcDecoder())
                        .addLast(new NettyClientHandler());
            }
        });

        Channel channel = connect(bootstrap, inetSocketAddress);
        channelMap.put(key, channel);
        return channel;
    }

    private static Channel connect(Bootstrap bootstrap, InetSocketAddress inetSocketAddress) throws ExecutionException, InterruptedException {
        CompletableFuture<Channel> completableFuture = new CompletableFuture<>();
        bootstrap.connect(inetSocketAddress).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                logger.warn("client connect success!");
                completableFuture.complete(future.channel());
            } else {
                throw new IllegalStateException("client connect fail");
            }
        });
        return completableFuture.get();
    }

}
