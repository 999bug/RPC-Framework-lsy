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
    KRYO_SERIALIZER(ConfigConst.KRYO_INT),
    JSON_SERIALIZER(ConfigConst.JSON_INT),
    HESSIAN_SERIALIZER(ConfigConst.HESSIAN_INT),
    PROTOBUF_SERIALIZER(ConfigConst.PROTOBUF_INT);

    private final int code;
}
