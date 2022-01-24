package com.ncst;

import com.ncst.dto.People;
import com.ncst.service.BaseServiceL;
import com.ncst.transfer.RpcClientProxy;
import com.ncst.transfer.netty.client.NettyClient;
import com.ncst.transfer.socket.RpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Lsy
 * @date 2022/1/24
 */
public class NettyClientMain {
    private static final Logger logger = LoggerFactory.getLogger(SocketClientMain.class);

    public static void main(String[] args) {
        RpcClient client = new NettyClient();
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client);

        BaseServiceL service = rpcClientProxy.getProxy(BaseServiceL.class);
        People people = new People("保安", 6, "小丹");
        String s = service.sayHello(people);
        logger.warn(s);
    }
}
