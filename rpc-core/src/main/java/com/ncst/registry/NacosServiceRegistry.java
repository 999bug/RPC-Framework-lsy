package com.ncst.registry;

import com.alibaba.nacos.api.exception.NacosException;
import com.ncst.common.NacosUtil;
import com.ncst.common.RpcError;
import com.ncst.exception.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * nacos 服务注册中心
 * @author Lsy
 * @date 2022/1/12
 */
public class NacosServiceRegistry implements ServiceRegistry{

    private static final Logger logger = LoggerFactory.getLogger(NacosServiceRegistry.class);

    @Override
    public void register(String serviceName, InetSocketAddress inetSocketAddress) {
        try {
            NacosUtil.registerService(serviceName, inetSocketAddress);
        }catch (NacosException e) {
            logger.error(e.getErrMsg());
            throw new RpcException(RpcError.REGISTRY_SERVER_FAIL);
        }
    }
}
