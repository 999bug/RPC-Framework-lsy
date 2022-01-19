package com.ncst.serializer;

import com.ncst.common.ConfigConst;
import com.ncst.common.RpcError;
import com.ncst.common.SerializerEnum;
import com.ncst.exception.RpcException;

/**
 * 通用序列化/反序列化接口
 *
 * @author Lsy
 * @date 2022/1/12
 */
public interface CommonSerializer {

    /**
     * 获取序列化方式
     */
    static CommonSerializer getSerializer(SerializerEnum serializerEnum) {
        switch (serializerEnum) {
            case JSON_SERIALIZER:
                return new JsonSerializer();
            case KRYO_SERIALIZER:
                return new KryoSerializer();
            case HESSIAN_SERIALIZER:
                return new HessianSerializer();
            case PROTOBUF_SERIALIZER:
                return new ProtobufSerializer();
            default:
                throw new RuntimeException("no type");
        }
    }

    static SerializerEnum getSerializer(int serializer) {
        switch (serializer) {
            case ConfigConst.kryoInt:
                return SerializerEnum.KRYO_SERIALIZER;
            case ConfigConst.jsonInt:
                return SerializerEnum.JSON_SERIALIZER;
            case ConfigConst.hessianInt:
                return SerializerEnum.HESSIAN_SERIALIZER;
            case ConfigConst.protobufInt:
                return SerializerEnum.PROTOBUF_SERIALIZER;
            default:
                throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
    }

    byte[] serialize(Object obj);

    Object deSerialize(byte[] bytes, Class<?> clazz);

    int getSerialCode();

}
