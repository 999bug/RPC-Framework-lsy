package com.ncst.transfer.socket.util;

import com.ncst.common.PackageType;
import com.ncst.common.RpcConfig;
import com.ncst.entity.RpcRequest;
import com.ncst.serializer.CommonSerializer;

import java.io.IOException;
import java.io.OutputStream;

import static com.ncst.util.CommonByteUtil.int2Byte;

/**
 * @author Lsy
 * @date 2022/1/13
 */
public class ObjectWriter {

    private ObjectWriter() {}

    /**
     *+---------------+---------------+-----------------+-------------+
     * |  Magic Number |  Package Type | Serializer Type | Data Length |
     * |    4 bytes    |    4 bytes    |     4 bytes     |   4 bytes   |
     * +---------------+---------------+-----------------+-------------+
     * |                          Data Bytes                           |
     * |                   Length: ${Data Length}                      |
     * +---------------------------------------------------------------+
     */
    public static void writeObj(OutputStream outputStream, Object obj, CommonSerializer serializer) throws IOException {
        outputStream.write(int2Byte(RpcConfig.getMagicNumber()));

        if (obj instanceof RpcRequest) {
            outputStream.write(int2Byte(PackageType.REQUEST.getCode()));
        }else {
            outputStream.write(int2Byte(PackageType.RESPONSE.getCode()));
        }

        outputStream.write(int2Byte(serializer.getSerialCode()));

        byte[] bytes = serializer.serialize(obj);
        outputStream.write(int2Byte(bytes.length));

        outputStream.write(bytes);
        outputStream.flush();
    }

}
