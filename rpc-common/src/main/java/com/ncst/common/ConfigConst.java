package com.ncst.common;

/**
 * 常量类用于序列化类型和负载均衡算法
 * @author Lsy
 */
public interface ConfigConst {
    String KRYO = "kryo";
    String JSON = "json";
    String HESSIAN = "hessian";
    String PROTOBUF = "protobuf";

    int KRYO_INT = 0;
    int JSON_INT = 1;
    int HESSIAN_INT = 2;
    int PROTOBUF_INT = 3;

    String RANDOM = "random";
}