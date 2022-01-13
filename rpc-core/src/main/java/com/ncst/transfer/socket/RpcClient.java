package com.ncst.transfer.socket;

import com.ncst.entity.RpcRequest;

/**
 * @author Lsy
 * @date 2022/1/13
 */
public interface RpcClient {

    Object sendRequest(RpcRequest rpcRequest);
}
