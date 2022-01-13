package com.ncst.discovery;

import java.net.InetSocketAddress;

/**
 * 服务发现接口
 * @author Lsy
 * @date 2022/1/13
 */
public interface ServiceDiscovery {

    InetSocketAddress lookupService(String serviceName);
}
