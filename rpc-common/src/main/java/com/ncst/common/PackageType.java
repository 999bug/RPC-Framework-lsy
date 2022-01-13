package com.ncst.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 包协议是request还是response
 * @author Lsy
 * @date 2022/1/13
 */
@AllArgsConstructor
@Getter
public enum PackageType {

    REQUEST(0),
    RESPONSE(1);

    private final int code;
}
