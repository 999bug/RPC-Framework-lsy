package com.ncst.transfer;

import com.ncst.provider.ServiceProvider;
import com.ncst.registry.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * @author Lsy
 * @date 2022/1/12
 */
public abstract class AbstractRpcServer implements RpcServer {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    protected String host;
    protected int port;

    protected ServiceRegistry serviceRegistry;
    protected ServiceProvider serviceProvider;

    public void scanServices() {

    }

    @Override
    public <T> void publishService(T service, String serviceName) {
        serviceProvider.addServiceProvider(service, serviceName);
        serviceRegistry.register(serviceName, new InetSocketAddress(host, port));
    }
}
