package com.ncst;

import com.ncst.common.RpcConfig;
import com.ncst.dto.People;
import com.ncst.service.BaseService;
import com.ncst.transfer.RpcClientProxy;
import com.ncst.transfer.socket.client.SocketClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Lsy
 * @date 2022/1/13
 */
public class SocketClientMain {
    private SocketClientMain() { }

    private static final Logger logger = LoggerFactory.getLogger(SocketClientMain.class);

    public static void main(String[] args) {
        SocketClient client = new SocketClient(RpcConfig.getSerializerEnum());
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client);

        BaseService service = rpcClientProxy.getProxy(BaseService.class);
        People people = new People("保安", 6, "小丹");
        String s = service.sayHello(people);
        logger.warn(s);

    }
}
