package com.ncst.transfer.netty.server;

import com.ncst.entity.RpcRequest;
import com.ncst.entity.RpcResponse;
import com.ncst.factory.SingletonFactory;
import com.ncst.handler.RequestHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 处理RpcRequest 的handler
 *
 * @author Lsy
 * @date 2022/1/21
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private static final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);
    private final RequestHandler requestHandler;

    public NettyServerHandler() {
        this.requestHandler = SingletonFactory.getInstance(RequestHandler.class);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext chc, RpcRequest rpcRequest) {
        try {
            if (rpcRequest.isHeartBeat()) {
                logger.info("receive client heartbeat..");
                return;
            }
            logger.info("server receive request {}", rpcRequest);
            Object handle = requestHandler.handle(rpcRequest);
            if (chc.channel().isActive() && chc.channel().isWritable()) {
                RpcResponse<Object> success = RpcResponse.success(handle, rpcRequest.getRequestId());
                chc.writeAndFlush(success);
            }
        }finally {
            ReferenceCountUtil.release(rpcRequest);
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
       if (evt instanceof IdleStateEvent) {
           IdleState state = ((IdleStateEvent) evt).state();
           if (state == IdleState.READER_IDLE) {
               logger.info("长时间未收到心跳包，断开连接...");
               ctx.close();
           }
       }else {
           super.userEventTriggered(ctx, evt);
       }
    }
}
