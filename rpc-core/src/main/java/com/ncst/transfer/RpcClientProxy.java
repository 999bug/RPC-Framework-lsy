package com.ncst.transfer;

import com.ncst.common.ResponseCode;
import com.ncst.common.RpcError;
import com.ncst.entity.RpcRequest;
import com.ncst.entity.RpcResponse;
import com.ncst.exception.RpcException;
import com.ncst.transfer.socket.RpcClient;
import com.ncst.transfer.socket.client.SocketClient;
import com.ncst.util.RpcMsgChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 * rpc 客户端动态代理
 *
 * @author Lsy
 * @date 2022/1/13
 */
public class RpcClientProxy implements InvocationHandler {
    private static final Logger logger = LoggerFactory.getLogger(RpcClientProxy.class);
    private final RpcClient client;

    public RpcClientProxy(RpcClient client) {
        this.client = client;
    }

    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        String clazzName = method.getDeclaringClass().getName();
        String methodName = method.getName();
        logger.info("{} invoke method {} ", clazzName, methodName);
        RpcRequest rpcRequest =
                new RpcRequest(UUID.randomUUID().toString(), clazzName, methodName, args, method.getParameterTypes(), false);

        RpcResponse response = null;
        if (client instanceof SocketClient) {
            response = (RpcResponse) client.sendRequest(rpcRequest);
        }

        RpcMsgChecker.check(rpcRequest, response);
        return response == null ? null : response.getData();
    }
}
