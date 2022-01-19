package com.ncst.transfer;

import com.ncst.annotation.Service;
import com.ncst.annotation.ServiceScan;
import com.ncst.common.RpcError;
import com.ncst.exception.RpcException;
import com.ncst.provider.ServiceProvider;
import com.ncst.registry.ServiceRegistry;
import com.ncst.util.ReflectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.util.Set;

/**
 * @author Lsy
 * @date 2022/1/12
 */
public abstract class AbstractRpcServer implements RpcServer {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    protected String host;
    protected int port;

    protected ServiceRegistry serviceRegistry;
    protected ServiceProvider serviceProvider;

    /**
     * 扫描注解
     */
    public void scanServices() {
        String mainClass = ReflectUtil.getMainClass();
        Class<?> zeroClass;
        try {
            zeroClass = Class.forName(mainClass);
            if (!zeroClass.isAnnotationPresent(ServiceScan.class)) {
                logger.error("start-up lacks serviceScan");
                throw new RpcException(RpcError.START_UP_LACKS_ANNOTATION);
            }
        } catch (ClassNotFoundException e) {
            logger.error("class not found");
            throw new RpcException(RpcError.UNKNOW_ERROR);
        }

        String basePackage = zeroClass.getAnnotation(ServiceScan.class).value();
        if ("".equals(basePackage)) {
            basePackage = mainClass.substring(0, mainClass.lastIndexOf("."));
        }

        Set<Class<?>> classes = ReflectUtil.getClasses(basePackage);
        // 发布注册实现类
        for (Class<?> clazz : classes) {
            if (clazz.isAnnotationPresent(Service.class)) {
                String serviceName = clazz.getAnnotation(Service.class).name();
                Object obj;
                try {
                    obj = clazz.getDeclaredConstructor().newInstance();
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    logger.error("create {} fail", clazz);
                    continue;
                }

                if ("".equals(serviceName)) {
                    Class<?>[] interfaces = clazz.getInterfaces();
                    for (Class<?> anInterface : interfaces) {
                        publishService(obj, anInterface.getCanonicalName());
                    }
                } else {
                    publishService(obj, serviceName);
                }
            }
        }
    }

    @Override
    public <T> void publishService(T service, String serviceName) {
        serviceProvider.addServiceProvider(service, serviceName);
        serviceRegistry.register(serviceName, new InetSocketAddress(host, port));
    }
}
