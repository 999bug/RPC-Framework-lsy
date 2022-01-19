package com.ncst.common;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.ncst.exception.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author Lsy
 * @date 2022/1/13
 */
public class NacosUtil {

    private NacosUtil() {
    }

    private static final Logger logger = LoggerFactory.getLogger(NacosUtil.class);
    private static final NamingService namingService;
    /**
     * nacos服务名set集合
     */
    private static final Set<String> serviceNameSets = new HashSet<>();
    private static InetSocketAddress address;

    private static final String SERVER_ADDRESS = RpcConfig.getHost() + ":" + RpcConfig.getPort();

    static {
        namingService = getNacosNamingService();
    }

    public static List<Instance> getAllInstance(String serviceName) throws NacosException {
        return namingService.getAllInstances(serviceName);
    }

    /**
     * 注册服务到 nacos
     *
     * @param serviceName   服务名
     * @param socketAddress host:port
     */
    public static void registerService(String serviceName, InetSocketAddress socketAddress) throws NacosException {
        namingService.registerInstance(serviceName, socketAddress.getHostName(), socketAddress.getPort());
        address = socketAddress;
        serviceNameSets.add(serviceName);
    }

    /**
     * 注销已连接的 nacos 服务
     */
    public static void clearRegistry() {
        if (!serviceNameSets.isEmpty() && address != null) {
            String hostName = address.getHostName();
            int port = address.getPort();
            Iterator<String> iterator = serviceNameSets.iterator();
            while (iterator.hasNext()) {
                String serviceName = iterator.next();
                try {
                    namingService.deregisterInstance(serviceName, hostName, port);
                } catch (NacosException e) {
                    logger.error("service {} deRegistration failure", serviceName);
                }
            }
        }
    }

    private static NamingService getNacosNamingService() {
        try {
            return NamingFactory.createNamingService(SERVER_ADDRESS);
        } catch (NacosException e) {
            logger.error(RpcError.FAILED_TO_CONNECT_TO_REGISTRY.getMessage());
            throw new RpcException(RpcError.FAILED_TO_CONNECT_TO_REGISTRY);
        }
    }
}
