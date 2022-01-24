package com.ncst.common;

import com.ncst.entity.RpcRequest;
import com.ncst.serializer.CommonSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 编码器
 *
 * @author Lsy
 * @date 2022/1/21
 */
public class RpcEnCoder extends MessageToByteEncoder {

    private final CommonSerializer serializer;

    public RpcEnCoder(CommonSerializer serializer) {
        this.serializer = serializer;
    }

    @Override
    protected void encode(ChannelHandlerContext chc, Object o, ByteBuf byteBuf) {
        byteBuf.writeInt(RpcConfig.getMagicNumber());
        if (o instanceof RpcRequest) {
            byteBuf.writeInt(PackageType.REQUEST);
        } else {
            byteBuf.writeInt(PackageType.RESPONSE);
        }
        byteBuf.writeInt(serializer.getSerialCode());
        byte[] bytes = serializer.serialize(o);
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);
    }
}
