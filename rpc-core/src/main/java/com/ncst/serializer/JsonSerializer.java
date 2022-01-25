package com.ncst.serializer;

import com.alibaba.fastjson.JSONObject;
import com.ncst.common.ConfigConst;

/**
 * @author Lsy
 * @date 2022/1/12
 */
public class JsonSerializer implements CommonSerializer {

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
        return ConfigConst.JSON_INT;
    }
}
