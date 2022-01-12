package com.ncst.factory;

import java.util.concurrent.ExecutorService;

/**
 * @author Lsy
 * @date 2022/1/12
 */
public class ThreadPollFactory {

    private ThreadPollFactory() {}

    public static ExecutorService createDefaultThreadPoll(String preFix) {
        return createDefaultThreadPoll(preFix, false);
    }

    public static ExecutorService createDefaultThreadPoll(String preFix, boolean daemon) {
        return null;
    }
}
