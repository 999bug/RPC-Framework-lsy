package com.ncst.serializer;

import com.ncst.common.ConfigConst;
import com.ncst.common.RpcError;
import com.ncst.common.SerializerEnum;
import com.ncst.exception.RpcException;

/**
 * 通用序列化/反序列化接口
 * @author Lsy
 * @date 2022/1/12
 */
public interface CommonSerializer {

    /**
     * 根据序列化枚举类型获取对应实现类
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

    /**
     * 根据 int 值获取对应序列化枚举类型
     */
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

    /**
     * 序列化
     * @param obj 序列化对象
     * @return 序列化字节数组
     */
    byte[] serialize(Object obj);

    /**
     * 反序列化
     * @param bytes 字节数组
     * @param clazz 转换对象的类型
     */
    Object deSerialize(byte[] bytes, Class<?> clazz);

    /**
     * @return 获取该序列化的 int 值
     */
    int getSerialCode();

}
