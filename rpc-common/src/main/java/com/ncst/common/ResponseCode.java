package com.ncst.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 方法调用相应状态码
 * @author Lsy
 * @date 2022/1/13
 */
@AllArgsConstructor
@Getter
public enum ResponseCode {
    SUCCESS(200, "Method call successful"),
    FAIL(500, "Method call fail"),
    METHOD_NOT_FOUND(404, "method not found"),
    CLASS_NOT_FOUND(405, "class not found");

    private final int code;
    private final String message;
}
