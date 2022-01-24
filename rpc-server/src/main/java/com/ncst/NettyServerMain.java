package com.ncst;

import com.ncst.annotation.ServiceScan;
import com.ncst.common.RpcConfig;
import com.ncst.serializer.CommonSerializer;
import com.ncst.transfer.AbstractRpcServer;
import com.ncst.transfer.RpcServer;
import com.ncst.transfer.netty.NettyServer;

/**
 * @author Lsy
 * @date 2022/1/21
 */
@ServiceScan
public class NettyServerMain {

    public static void main(String[] args) {
        RpcServer server = new NettyServer("127.0.0.1", 9191, RpcConfig.getSerializerEnum());
        server.start();
    }
}
