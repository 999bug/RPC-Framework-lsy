package com.ncst.transfer.netty.client;

import com.ncst.common.RpcConfig;
import com.ncst.entity.RpcRequest;
import com.ncst.entity.RpcResponse;
import com.ncst.factory.SingletonFactory;
import com.ncst.serializer.CommonSerializer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * netty client 处理器
 * @author Lsy
 * @date 2022/1/24
 */
public class NettyClientHandler extends SimpleChannelInboundHandler<RpcResponse> {

   private static final Logger logger = LoggerFactory.getLogger(NettyClientHandler.class);

   private final UnprocessedRequests unprocessedRequests;

   public NettyClientHandler() {
       this.unprocessedRequests = SingletonFactory.getInstance(UnprocessedRequests.class);
   }


    @Override
    protected void channelRead0(ChannelHandlerContext chc, RpcResponse response) {
        try {
            logger.info("receive client msg {}", response);
            unprocessedRequests.complete(response);
        }finally {
            ReferenceCountUtil.release(response);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error("invoke fail {}", cause.getMessage());
        ctx.close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.WRITER_IDLE) {
                logger.info("send heartbeat [{}]", ctx.channel().remoteAddress());
                Channel channel = ChannelProvider.get((InetSocketAddress) ctx.channel().remoteAddress(), CommonSerializer.getSerializer(RpcConfig.getSerializerEnum()));
                RpcRequest rpcRequest = new RpcRequest();
                rpcRequest.setHeartBeat(true);
                channel.writeAndFlush(rpcRequest).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
