package com.ncst.handler;

import com.ncst.factory.com.ncst.entity.RpcRequest;
import com.ncst.provider.ServiceProvider;
import com.ncst.provider.ServiceProviderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * 执行过程调用处理器
 *
 * @author Lsy
 * @date 2022/1/12
 */
public class RequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static final ServiceProvider serviceProvider = new ServiceProviderImpl();

    public Object handle(RpcRequest rpcRequest) {
        Object service = RequestHandler.serviceProvider.getServiceProvider(rpcRequest.getInterfaceName());
        return invokeTargetMethod(rpcRequest, service);
    }

    private Object invokeTargetMethod(RpcRequest rpcRequest, Object service) {
        Object result;
        try {
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
            result = method.invoke(service, rpcRequest.getParams());
            logger.warn("服务:{} 成功调用方法:{}", rpcRequest.getInterfaceName(), rpcRequest.getMethodName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
