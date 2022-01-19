package com.ncst.hook;

import com.ncst.common.NacosUtil;
import com.ncst.factory.ThreadPollFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Lsy
 * @date 2022/1/19
 */
public class ShutdownHook {
    private static final Logger logger = LoggerFactory.getLogger(ShutdownHook.class);

    private static final ShutdownHook shutdownHook = new ShutdownHook();

    public static ShutdownHook getShutdownHook() {
        return shutdownHook;
    }

    public void addClearAllHook() {
        logger.info("关闭后将自动注销所有服务");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            NacosUtil.clearRegistry();
            ThreadPollFactory.shutDownAll();
        }));
    }
}
