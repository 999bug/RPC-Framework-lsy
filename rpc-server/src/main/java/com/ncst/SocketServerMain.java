package com.ncst;

import com.ncst.common.RpcConfig;
import com.ncst.transfer.RpcServer;
import com.ncst.transfer.socket.server.SocketServer;

/**
 * @author Lsy
 * @date 2022/1/12
 */
public class SocketServerMain {
    private static final String host = "127.0.0.1";
    private static final int port = 9191;

    public static void main(String[] args) {
        RpcServer server = new SocketServer(host, port, RpcConfig.getSerializerEnum());
        server.start();
    }
}
