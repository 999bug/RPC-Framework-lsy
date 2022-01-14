package com.ncst.util;

import com.ncst.common.ResponseCode;
import com.ncst.common.RpcError;
import com.ncst.entity.RpcRequest;
import com.ncst.entity.RpcResponse;
import com.ncst.exception.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 检查相应与请求
 * @author Lsy
 * @date 2022/1/13
 */
public class RpcMsgChecker {
    private RpcMsgChecker() {}

    private static final Logger logger = LoggerFactory.getLogger(RpcMsgChecker.class);

    public static void check(RpcRequest rpcRequest, RpcResponse response) {
        String clazzName = rpcRequest.getInterfaceName();
        if (response == null) {
            logger.error("invoke server fail: {}", clazzName);
            throw new RpcException(RpcError.SERVICE_INVOKE_FAIL, "clazzName: " + clazzName);
        }

        if (!rpcRequest.getRequestId().equals(response.getRequestId())) {
            logger.error("response and requestId not match: {}", clazzName);
            throw new RpcException(RpcError.RESPONSE_NOT_MATCH, "clazzName: " + clazzName);
        }

        if (response.getStatusCode() == null || !response.getStatusCode().equals(ResponseCode.SUCCESS.getCode())) {
            logger.error("invoke service fail: {} response {}", clazzName, response);
            throw new RpcException(RpcError.SERVICE_INVOKE_FAIL, "clazzName: " + clazzName);
        }
    }
}
