package com.ncst;

import com.ncst.common.RpcConfig;
import org.junit.Test;

/**
 * @author Lsy
 * @date 2022/1/13
 */
public class testMain {

    @Test
    public void test() {
        RpcConfig.init();
        System.out.println("RpcConfig.getLoadBalance() = " + RpcConfig.getLoadBalance());
        System.out.println("RpcConfig.getSerializerEnum() = " + RpcConfig.getSerializerEnum());
        System.out.println("RpcConfig.getMagicNumber() = " + RpcConfig.getMagicNumber());
        System.out.println("RpcConfig.getHost() = " + RpcConfig.getHost());
        System.out.println("RpcConfig.getPort() = " + RpcConfig.getPort());
    }
}
