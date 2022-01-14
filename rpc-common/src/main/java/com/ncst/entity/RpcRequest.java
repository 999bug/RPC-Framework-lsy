package com.ncst.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Lsy
 * @date 2022/1/12
 */
@Data
@AllArgsConstructor
public class RpcRequest implements Serializable {
    /**
     * 请求号
     */
    private String requestId;
    /**
     * 待调用接口名称
     */
    private String interfaceName;
    /**
     * 待调用接口方法
     */
    private String methodName;
    /**
     * 调用方法的参数
     */
    private Object[] params;
    /**
     * 调用方法的参数类型
     */
    private Class<?>[] paramTypes;
    /**
     * 是否是心跳包
     */
    private boolean heartBeat;

}
