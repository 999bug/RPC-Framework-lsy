package com.ncst.serializer;

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
    static CommonSerializer getSerial(Serializer serializer) {
        switch (serializer) {
            case JSON_SERIALIZER:
                return new KryoSerializer();
            case KRYO_SERIALIZER:
                return new JsonSerializer();
            case HESSIAN_SERIALIZER:
                return new HessianSerializer();
            case PROTOBUF_SERIALIZER:
                return new ProtobufSerializer();
            default:
                throw new RuntimeException("no type");
        }
    }

    byte[] serialize(Object obj);

    Object deSerialize(byte[] bytes, Class<?> clazz);

    int getSerialCode();



}
