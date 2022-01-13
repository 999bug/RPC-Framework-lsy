package com.ncst.transfer.socket.client;

import com.ncst.balance.LoadBalance;
import com.ncst.common.RpcConfig;
import com.ncst.common.SerializerEnum;
import com.ncst.discovery.NacosServiceDiscovery;
import com.ncst.discovery.ServiceDiscovery;
import com.ncst.entity.RpcRequest;
import com.ncst.serializer.CommonSerializer;
import com.ncst.transfer.socket.RpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Lsy
 * @date 2022/1/12
 */
public class SocketClient implements RpcClient {

    private static final Logger logger = LoggerFactory.getLogger(SocketClient.class);
    private final CommonSerializer commonSerializer;
    private final ServiceDiscovery serviceDiscovery;

    public SocketClient() {
        this(RpcConfig.getSerializerEnum(), RpcConfig.getLoadBalance());
    }

    public SocketClient(SerializerEnum serializerEnum) {
        this(serializerEnum, RpcConfig.getLoadBalance());
    }

    public SocketClient(LoadBalance loadBalance) {
        this(RpcConfig.getSerializerEnum(), loadBalance);
    }

    public SocketClient(SerializerEnum serializerEnum, LoadBalance loadBalance) {
        RpcConfig.init();
        this.commonSerializer = CommonSerializer.getSerial(serializerEnum);
        this.serviceDiscovery = new NacosServiceDiscovery(loadBalance);
    }

    @Override
    public Object sendRequest(RpcRequest rpcRequest) {
        return null;
    }
}
