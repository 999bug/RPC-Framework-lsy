package com.ncst.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 序列化形式
 * @author Lsy
 * @date 2022/1/12
 */
@Getter
@AllArgsConstructor
public enum SerializerEnum {
    KRYO_SERIALIZER(0),
    JSON_SERIALIZER(1),
    HESSIAN_SERIALIZER(2),
    PROTOBUF_SERIALIZER(3);

    private final int code;
}
