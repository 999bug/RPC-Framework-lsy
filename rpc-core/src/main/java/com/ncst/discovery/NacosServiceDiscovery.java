package com.ncst.discovery;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.ncst.balance.LoadBalance;
import com.ncst.common.NacosUtil;
import com.ncst.common.RpcConfig;
import com.ncst.common.RpcError;
import com.ncst.exception.RpcException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author Lsy
 * @date 2022/1/13
 */
public class NacosServiceDiscovery implements ServiceDiscovery{

    private final Logger logger = LoggerFactory.getLogger(NacosServiceDiscovery.class);
    private final LoadBalance loadBalance;

    public NacosServiceDiscovery() {
        this(RpcConfig.getLoadBalance());
    }

    public NacosServiceDiscovery(LoadBalance loadBalance) {
        this.loadBalance = loadBalance;
    }

    @Override
    public InetSocketAddress lookupService(String serviceName) {
        try {
            List<Instance> instances = NacosUtil.getAllInstance(serviceName);
            if (instances.size() == 0) {
                logger.error("{} nacos service not found", serviceName);
                throw new RpcException(RpcError.NACOS_SERVICE_NOT_FOUND);
            }
            Instance select = loadBalance.select(instances);
            return new InetSocketAddress(select.getIp(), select.getPort());
        } catch (NacosException e) {
           logger.error("get service error {}", e.getMessage());
        }
        return null;
    }
}
