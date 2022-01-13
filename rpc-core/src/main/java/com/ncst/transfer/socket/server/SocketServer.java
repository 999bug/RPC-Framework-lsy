package com.ncst.transfer.socket.server;

import com.ncst.common.RpcConfig;
import com.ncst.factory.ThreadPollFactory;
import com.ncst.handler.RequestHandler;
import com.ncst.provider.ServiceProviderImpl;
import com.ncst.registry.NacosServiceRegistry;
import com.ncst.common.SerializerEnum;
import com.ncst.serializer.CommonSerializer;
import com.ncst.transfer.AbstractRpcServer;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

/**
 * socket 方式远程方法调用的提供者
 *
 * @author Lsy
 * @date 2022/1/12
 */
public class SocketServer extends AbstractRpcServer {

    private final ExecutorService executorService;
    private final CommonSerializer commonSerializer;
    private final RequestHandler requestHandler = new RequestHandler();

    public SocketServer(String host, int port) {
        this(host, port, SerializerEnum.KRYO_SERIALIZER );
    }

    public SocketServer(String host, int port, SerializerEnum serializerEnum) {
        RpcConfig.init();
        this.host = host;
        this.port = port;
        this.commonSerializer = CommonSerializer.getSerial(serializerEnum);
        executorService = ThreadPollFactory.createDefaultThreadPoll("socket_rpc_server");
        this.serviceRegistry = new NacosServiceRegistry();
        this.serviceProvider = new ServiceProviderImpl();
        scanServices();

    }

    @Override
    public void start() {
        try (ServerSocket serverSocket = new ServerSocket()) {
            serverSocket.bind(new InetSocketAddress(host, port));
            logger.info("start server..");
            Socket socket;
            while ((socket = serverSocket.accept()) != null) {
                logger.warn("{}:{} consumer connect!", host, port);
                executorService.execute(new SocketRequestHandlerThread(socket, requestHandler, commonSerializer));
            }
            executorService.shutdown();
        } catch (Exception e) {
            logger.error("socketServer start have error {} ", e.getMessage());
        }
    }
}
