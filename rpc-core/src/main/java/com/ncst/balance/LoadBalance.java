package com.ncst.balance;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;

/**
 * @author Lsy
 * @date 2022/1/13
 */
public interface LoadBalance {

    Instance select(List<Instance> instances);
}
