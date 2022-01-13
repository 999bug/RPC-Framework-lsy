package com.ncst.common;

import com.ncst.balance.LoadBalance;
import com.ncst.balance.RandomLoadBalancer;
import com.ncst.exception.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * 读取配置文件
 *
 * @author Lsy
 * @date 2022/1/13
 */
public class RpcConfig {

    private RpcConfig() { }

    private static final Logger logger = LoggerFactory.getLogger(RpcConfig.class);
    private static final int magicNumber = 0xCAFEBABE;
    private static String serializer = "kryo";
    private static String loadBalance = "random";
    private static String host = "192.168.46.22";
    private static int port = 8848;

    public static void init() {
        Properties prop = new Properties();
        //绝对路径
        try (InputStream in = new FileInputStream("D:\\code\\jam\\RPC-Framework-lsy\\conf\\rpc-site.conf");
             BufferedReader bf = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            prop.load(bf);
        } catch (IOException e) {
            logger.error("Fail to read the program config file.");
        }
        serializer = prop.getProperty("rpc.default.serializer", serializer);
        loadBalance = prop.getProperty("rpc.default.loadBalance", loadBalance);
        host = prop.getProperty("rpc.nacos.host", host);
        port = Integer.parseInt(prop.getProperty("rpc.nacos.port", String.valueOf(port)));
    }

    private static LoadBalance getLoadbalance(String loadBalance) {
        switch (loadBalance) {
            case ConfigConst.random:
                return new RandomLoadBalancer();
            default:
                logger.error("error message {}", RpcError.LOAD_BALANCE_NOT_FOUND);
                throw new RpcException(RpcError.LOAD_BALANCE_NOT_FOUND);
        }
    }

    private static SerializerEnum getSerializer(String serializer) {
        switch (serializer) {
            case ConfigConst.kryo:
                return SerializerEnum.KRYO_SERIALIZER;
            case ConfigConst.hessian:
                return SerializerEnum.HESSIAN_SERIALIZER;
            case ConfigConst.json:
                return SerializerEnum.JSON_SERIALIZER;
            case ConfigConst.protobuf:
                return SerializerEnum.PROTOBUF_SERIALIZER;
            default:
                logger.error("error message {}", RpcError.SERIALIZER_NOT_FOUND);
                throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
    }

    public static SerializerEnum getSerializerEnum() {
        return getSerializer(serializer.toLowerCase());
    }

    public static LoadBalance getLoadBalance() {
        return getLoadbalance(loadBalance.toLowerCase());
    }

    public static int getMagicNumber() {
        return magicNumber;
    }

    public static String getHost() {
        return host;
    }

    public static int getPort() {
        return port;
    }

    private interface ConfigConst {
        String kryo = "kryo";
        String json = "json";
        String hessian = "hessian";
        String protobuf = "protobuf";
        String random = "random";
    }
}
