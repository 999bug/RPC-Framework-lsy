package com.ncst.common;

import com.ncst.entity.RpcRequest;
import com.ncst.entity.RpcResponse;
import com.ncst.exception.RpcException;
import com.ncst.serializer.CommonSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 解码器
 *
 * @author Lsy
 * @date 2022/1/21
 */
public class RpcDecoder extends ReplayingDecoder {

    private static final Logger logger = LoggerFactory.getLogger(RpcDecoder.class);

    @Override
    protected void decode(ChannelHandlerContext chc, ByteBuf byteBuf, List<Object> list) {
        int magicNum = byteBuf.readInt();
        if (magicNum != RpcConfig.getMagicNumber()) {
            logger.error("no other protocol package");
            throw new RpcException(RpcError.UNKNOWN_PROTOCOL);
        }

        int packageCode = byteBuf.readInt();
        Class<?> packageClass;
        switch (packageCode) {
            case PackageType.REQUEST:
                packageClass = RpcRequest.class;
                break;
            case PackageType.RESPONSE:
                    packageClass = RpcResponse.class;
                    break;
            default:
                logger.error("不识别的数据包");
                throw new RpcException(RpcError.UNKNOWN_PACKAGE_TYPE);
        }

        int serializerCode = byteBuf.readInt();
        CommonSerializer serializer = CommonSerializer.getSerializer(CommonSerializer.getSerializer(serializerCode));

        int length = byteBuf.readInt();
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);
        Object obj = serializer.deSerialize(bytes, packageClass);
        list.add(obj);
    }
}
