package com.ncst.transfer.socket.util;

import com.ncst.common.PackageType;
import com.ncst.common.RpcConfig;
import com.ncst.common.RpcError;
import com.ncst.entity.RpcRequest;
import com.ncst.entity.RpcResponse;
import com.ncst.exception.RpcException;
import com.ncst.serializer.CommonSerializer;
import com.ncst.util.CommonByteUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

import static com.ncst.util.CommonByteUtil.readData;


/**
 * 从InputStream中读取字节反序列
 *
 * @author Lsy
 * @date 2022/1/13
 */
public class ObjectReader {

    private ObjectReader() {
    }

    private static final Logger logger = LoggerFactory.getLogger(CommonByteUtil.class);

    /**
     * +---------------+---------------+-----------------+-------------+
     * |  Magic Number |  Package Type | Serializer Type | Data Length |
     * |    4 bytes    |    4 bytes    |     4 bytes     |   4 bytes   |
     * +---------------+---------------+-----------------+-------------+
     * |                          Data Bytes                           |
     * |                   Length: ${Data Length}                      |
     * +---------------------------------------------------------------+
     */
    public static Object readObj(InputStream inputStream) throws IOException {
        byte[] bytes = new byte[4];
        int magic = getResult(inputStream, bytes);
        if (magic != RpcConfig.getMagicNumber()) {
            logger.error(RpcError.UNKNOWN_PROTOCOL.getMessage());
            throw new RpcException(RpcError.UNKNOW_ERROR);
        }

        int packageCode = getResult(inputStream, bytes);
        Class<?> packageClass;
        switch (packageCode) {
            case PackageType.REQUEST:
                packageClass = RpcRequest.class;
                break;
            case PackageType.RESPONSE:
                packageClass = RpcResponse.class;
                break;
            default:
                logger.error(RpcError.UNKNOWN_PACKAGE_TYPE.getMessage());
                throw new RpcException(RpcError.UNKNOWN_PACKAGE_TYPE);
        }

        int serializerCode = getResult(inputStream, bytes);
        CommonSerializer serializer = CommonSerializer.getSerializer(CommonSerializer.getSerializer(serializerCode));

        int length = getResult(inputStream, bytes);

        byte[] data = new byte[length];
        CommonByteUtil.readData(inputStream, data);
        return serializer.deSerialize(data, packageClass);
    }

    private static int getResult(InputStream inputStream, byte[] bytes) throws IOException {
        readData(inputStream, bytes);
        return bytesToInt(bytes);
    }

    public static int bytesToInt(byte[] src) {
        int value;
        value = ((src[0] & 0xFF) << 24)
                | ((src[1] & 0xFF) << 16)
                | ((src[2] & 0xFF) << 8)
                | (src[3] & 0xFF);
        return value;
    }
}
