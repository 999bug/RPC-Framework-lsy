package com.ncst.registry;

import java.net.InetSocketAddress;

/**
 * 服务注册接口
 * @author Lsy
 * @date 2022/1/12
 */
public interface ServiceRegistry {

    void register(String serviceName, InetSocketAddress inetSocketAddress);
}
