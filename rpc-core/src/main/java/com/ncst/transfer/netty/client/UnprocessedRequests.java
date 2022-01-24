package com.ncst.transfer.netty.client;

import com.ncst.entity.RpcResponse;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 缓存未处理的 map
 * @author Lsy
 * @date 2022/1/24
 */
public class UnprocessedRequests {

    /**
     * key为requestId value为future 存放未处理的请求
     */
    private static final Map<String, CompletableFuture<RpcResponse>> unProcessedResponse;

    static {
        unProcessedResponse = new ConcurrentHashMap<>();
    }

    public void put(String requestId, CompletableFuture<RpcResponse> future) {
        unProcessedResponse.put(requestId, future);
    }

    public void remove(String requestId) {
        unProcessedResponse.remove(requestId);
    }

    public void complete(RpcResponse response) {
        CompletableFuture<RpcResponse> future = unProcessedResponse.remove(response.getRequestId());
        if (future != null) {
            future.complete(response);
        }else {
            throw new IllegalStateException();
        }
    }
}
