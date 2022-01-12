package com.ncst.transfer.socket.server;

import com.ncst.factory.ThreadPollFactory;
import com.ncst.provider.ServiceProviderImpl;
import com.ncst.registry.NacosServiceRegistry;
import com.ncst.serializer.Serializer;
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
    private final Serializer serializer;

    public SocketServer(String host, int port) {
        this(host, port, Serializer.DEFAULT_SERIALIZER);
    }

    public SocketServer(String host, int port, Serializer serializer) {
        this.host = host;
        this.port = port;
        this.serializer = serializer;
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
            while ((serverSocket.accept()) != null) {
                logger.warn("{}:{} consumer connect!", host, port);
                executorService.execute();
            }


        } catch (Exception e) {
            logger.error("socketServer start have error {} ", e.getMessage());
        }
    }
}
