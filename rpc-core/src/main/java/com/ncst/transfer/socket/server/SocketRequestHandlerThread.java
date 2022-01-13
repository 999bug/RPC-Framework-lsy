package com.ncst.transfer.socket.server;

import com.ncst.entity.RpcRequest;
import com.ncst.entity.RpcResponse;
import com.ncst.common.SerializerEnum;
import com.ncst.handler.RequestHandler;
import com.ncst.serializer.CommonSerializer;
import com.ncst.transfer.socket.util.ObjectReader;
import com.ncst.transfer.socket.util.ObjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 处理RpcRequest的工作线程
 *
 * @author Lsy
 * @date 2022/1/12
 */
public class SocketRequestHandlerThread implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(SocketRequestHandlerThread.class);
    private Socket socket;
    private RequestHandler requestHandler;
    private CommonSerializer commonSerializer;

    public SocketRequestHandlerThread(Socket socket, RequestHandler requestHandler, CommonSerializer commonSerializer) {
        this.socket = socket;
        this.requestHandler = requestHandler;
        this.commonSerializer = commonSerializer;
    }

    @Override
    public void run() {
        try (InputStream inputStream = socket.getInputStream();
             OutputStream outputStream = socket.getOutputStream()) {
            RpcRequest rpcRequest = (RpcRequest) ObjectReader.readObj(inputStream);
            Object result = requestHandler.handle(rpcRequest);
            RpcResponse<Object> response = RpcResponse.success(result, rpcRequest.getRequestId());
            ObjectWriter.writeObj(outputStream, response, commonSerializer);
        } catch (Exception e) {
            logger.error("Call error {}", e.getMessage());
        }
    }
}
