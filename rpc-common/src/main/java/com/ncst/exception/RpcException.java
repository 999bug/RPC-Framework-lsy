package com.ncst.exception;

import com.ncst.common.RpcError;

/**
 * @author Lsy
 * @date 2022/1/13
 */
public class RpcException extends RuntimeException {

    public RpcException(RpcError error, String detail) {
        super(error.getMessage() + ":" + detail);
    }

    public RpcException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public RpcException(RpcError rpcError) {
        super(rpcError.getMessage());
    }

}
