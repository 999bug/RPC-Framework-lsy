package com.ncst.serializer;

import com.ncst.common.ConfigConst;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Lsy
 * @date 2022/1/12
 */
public class ProtobufSerializer implements CommonSerializer {
    private final LinkedBuffer BUFFER = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
    private final Map<Class<?>, Schema<?>> schemaMap = new ConcurrentHashMap<>();

    @Override
    public byte[] serialize(Object obj) {
        Class<?> clazz = obj.getClass();
        Schema schema = getSchema(clazz);
        byte[] bytes;
        try {
             bytes = ProtostuffIOUtil.toByteArray(obj, schema, BUFFER);
        }finally {
            BUFFER.clear();
        }
        return bytes;
    }

    @Override
    public Object deSerialize(byte[] bytes, Class<?> clazz) {
        Schema schema = getSchema(clazz);
        Object obj = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(bytes, obj, schema);
        return obj;
    }

    @Override
    public int getSerialCode() {
        return ConfigConst.PROTOBUF_INT;
    }

    private Schema<?> getSchema(Class<?> clazz) {
        Schema<?> schema = schemaMap.get(clazz);
        if (schema == null) {
           schema = RuntimeSchema.getSchema(clazz);
           if (schema != null) {
               schemaMap.put(clazz, schema);
           }
        }
        return schema;
    }
}
