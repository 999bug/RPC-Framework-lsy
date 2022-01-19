package com.ncst.transfer.socket.util;

import com.ncst.common.PackageType;
import com.ncst.common.RpcConfig;
import com.ncst.entity.RpcRequest;
import com.ncst.serializer.CommonSerializer;

import java.io.IOException;
import java.io.OutputStream;


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
        int magicNumber = RpcConfig.getMagicNumber();
        outputStream.write(int2Byte(magicNumber));

        if (obj instanceof RpcRequest) {
            outputStream.write(int2Byte(PackageType.REQUEST));
        }else {
            outputStream.write(int2Byte(PackageType.RESPONSE));
        }

        outputStream.write(int2Byte(serializer.getSerialCode()));

        byte[] bytes = serializer.serialize(obj);
        outputStream.write(int2Byte(bytes.length));

        outputStream.write(bytes);
        outputStream.flush();
    }

    private static byte[] int2Byte(int value) {
        byte[] src = new byte[4];
        src[0] = (byte) ((value>>24) & 0xFF);
        src[1] = (byte) ((value>>16)& 0xFF);
        src[2] = (byte) ((value>>8)&0xFF);
        src[3] = (byte) (value & 0xFF);
        return src;
    }

}
