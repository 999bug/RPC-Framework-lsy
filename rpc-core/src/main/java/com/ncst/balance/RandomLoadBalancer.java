package com.ncst.balance;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 随机轮询
 * @author Lsy
 * @date 2022/1/13
 */
public class RandomLoadBalancer implements LoadBalance {
    @Override
    public Instance select(List<Instance> instances) {
        return instances.get(ThreadLocalRandom.current().nextInt(instances.size()));
    }
}
