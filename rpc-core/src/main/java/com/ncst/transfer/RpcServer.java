package com.ncst.transfer;

/**
 * @author Lsy
 * @date 2022/1/12
 */
public interface RpcServer {

    void start();

    <T> void publishService(T service, String serviceName);
}
