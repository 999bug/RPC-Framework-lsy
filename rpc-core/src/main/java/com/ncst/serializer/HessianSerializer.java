package com.ncst.serializer;

import com.ncst.common.ConfigConst;

/**
 * @author Lsy
 * @date 2022/1/12
 */
public class HessianSerializer implements CommonSerializer {
    @Override
    public byte[] serialize(Object obj) {
        return new byte[0];
    }

    @Override
    public Object deSerialize(byte[] bytes, Class<?> clazz) {
        return null;
    }

    @Override
    public int getSerialCode() {
        return ConfigConst.HESSIAN_INT;
    }
}
