package com.ncst.transfer.socket.client;

import com.ncst.balance.LoadBalance;
import com.ncst.common.RpcConfig;
import com.ncst.common.RpcError;
import com.ncst.common.SerializerEnum;
import com.ncst.discovery.NacosServiceDiscovery;
import com.ncst.discovery.ServiceDiscovery;
import com.ncst.entity.RpcRequest;
import com.ncst.entity.RpcResponse;
import com.ncst.exception.RpcException;
import com.ncst.serializer.CommonSerializer;
import com.ncst.transfer.socket.RpcClient;
import com.ncst.transfer.socket.util.ObjectReader;
import com.ncst.transfer.socket.util.ObjectWriter;
import com.ncst.util.RpcMsgChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author Lsy
 * @date 2022/1/12
 */
public class SocketClient implements RpcClient {

    private static final Logger logger = LoggerFactory.getLogger(SocketClient.class);
    private final CommonSerializer serializer;
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
        this.serializer = CommonSerializer.getSerializer(serializerEnum);
        this.serviceDiscovery = new NacosServiceDiscovery(loadBalance);
    }

    @Override
    public Object sendRequest(RpcRequest rpcRequest) {
        if (serializer == null) {
            logger.error(RpcError.SERIALIZER_NOT_FOUND.getMessage());
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }

        InetSocketAddress address = serviceDiscovery.lookupService(rpcRequest.getInterfaceName());
        try (Socket socket = new Socket()) {
            socket.connect(address);
            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();
            ObjectWriter.writeObj(outputStream, rpcRequest, serializer);
            Object obj = ObjectReader.readObj(inputStream);
            RpcResponse response = (RpcResponse) obj;
            RpcMsgChecker.check(rpcRequest, response);
            return response;
        } catch (IOException e) {
           e.printStackTrace();
        }
        return null;
    }
}
