package com.ncst.common;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Lsy
 * @date 2022/1/13
 */
public class NacosUtil {

    private NacosUtil() {}

    private static final Logger logger = LoggerFactory.getLogger(NacosUtil.class);
    private static final NamingService namingService;
    private static final String SERVER_ADDR = RpcConfig.getHost() + ":" + RpcConfig.getPort();

    static {
        namingService = getNacosNamingService();
    }

    public static List<Instance> getAllInstance(String serviceName) throws NacosException {
        return namingService.getAllInstances(serviceName);
    }
    private static NamingService getNacosNamingService() {
        return null;
    }
}
