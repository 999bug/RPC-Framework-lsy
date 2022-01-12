package com.ncst.serializer;

/**
 * 序列化形式
 * @author Lsy
 * @date 2022/1/12
 */
public enum Serializer {
    KRYO_SERIALIZER(0),
    JSON_SERIALIZER(1),
    HESSIAN_SERIALIZER(2),
    PROTOBUF_SERIALIZER(3),
    DEFAULT_SERIALIZER(4);

    private final int rank;

    Serializer(int rank) {
        this.rank = rank;
    }

    public Serializer getDefault() {
        return DEFAULT_SERIALIZER;
    }

}
