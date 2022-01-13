package com.ncst.transfer.socket.util;

import com.ncst.common.RpcConfig;
import com.ncst.common.RpcError;
import com.ncst.exception.RpcException;
import com.ncst.util.CommonByteUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

import static com.ncst.util.CommonByteUtil.byte2Int;
import static com.ncst.util.CommonByteUtil.readData;

/**
 * 从InputStream中读取字节反序列
 * @author Lsy
 * @date 2022/1/13
 */
public class ObjectReader {

    private ObjectReader() {}

    private static final Logger logger = LoggerFactory.getLogger(CommonByteUtil.class);

    public static Object readObj(InputStream inputStream) throws IOException {
        byte[] bytes = new byte[4];
        readData(inputStream, bytes);
        int magic = CommonByteUtil.byte2Int(bytes);
        if (magic != RpcConfig.getMagicNumber()) {
            logger.error(RpcError.UNKNOWN_PROTOCOL.getMessage());
            throw new RpcException(RpcError.UNKNOW_ERROR);
        }

        return new Object();
    }
}
