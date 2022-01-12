package com.ncst.provider;

/**
 * 保存和提供服务实例对象
 * @author Lsy
 * @date 2022/1/12
 */
public interface ServiceProvider {

    <T> void addServiceProvider(T service, String serviceName);

    Object getServiceProvider(String serviceName);
}