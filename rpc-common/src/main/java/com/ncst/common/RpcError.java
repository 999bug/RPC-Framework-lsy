package com.ncst.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Lsy
 * @date 2022/1/13
 */
@Getter
@AllArgsConstructor
public enum RpcError {

    UNKNOW_ERROR("unknow error"),
    UNKNOWN_PROTOCOL("unknown protocol"),
    UNKNOWN_SERIALIZER("unknown serializer"),
    UNKNOWN_PACKAGE_TYPE("unknown package type"),

    FAILED_TO_CONNECT_TO_REGISTRY("Failed to connect to the registry"),
    REGISTRY_SERVER_FAIL("registry server fail"),
    SERVICE_INVOKE_FAIL("service invoke fail"),
    SERVICE_NOT_IMPLEMENT_INTERFACE("The registration service does not implement an interface"),
    RESPONSE_NOT_MATCH("response and requestId not match"),

    SERVICE_SCAN_PACKAGE_NOT_FOUND("serviceScan not found"),
    NACOS_SERVICE_NOT_FOUND("nacos service not found"),
    LOAD_BALANCE_NOT_FOUND("load balance not found"),
    SERIALIZER_NOT_FOUND("serializer not found");



    private final String message;
    }
