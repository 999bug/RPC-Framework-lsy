package com.ncst.factory;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.*;

/**
 * 创建线程池工具类，借鉴 JavaGuide
 * @author Lsy
 * @date 2022/1/12
 */
public class ThreadPollFactory {

    /**
     * 核心线程池数
     */
    private static final int CORE_POOL_SIZE = 10;
    /**
     * 最大线程数
     */
    private static final int MAX_POOL_SIZE = 100;
    /**
     * 线程空闲时间
     */
    private static final int KEEP_ALIVE_TIME = 1;
    /**
     * 阻塞队列容量
     */
    private static final int QUEUE_CAPACITY = 100;
    private static final Logger logger = LoggerFactory.getLogger(ThreadPollFactory.class);
    private static final Map<String, ExecutorService> THREAD_POLL_MAPS = new ConcurrentHashMap<>();

    private ThreadPollFactory() {}

    public static ExecutorService createDefaultThreadPoll() {
        return createDefaultThreadPoll("pool-", false);
    }

    public static ExecutorService createDefaultThreadPoll(String preFix) {
        return createDefaultThreadPoll(preFix, false);
    }

    public static ExecutorService createDefaultThreadPoll(String preFix, boolean daemon) {
        ExecutorService pool =
                THREAD_POLL_MAPS.computeIfAbsent(preFix, m -> createThreadPool(preFix, daemon));
        if (pool.isTerminated() || pool.isShutdown()) {
            THREAD_POLL_MAPS.remove(preFix);
            pool = createThreadPool(preFix, daemon);
            THREAD_POLL_MAPS.put(preFix, pool);
        }
        return pool;
    }

    public static void shutDownAll() {
        logger.warn("close all thread pool!");
        THREAD_POLL_MAPS.entrySet().parallelStream().forEach(entry -> {
            ExecutorService executorService = entry.getValue();
            executorService.shutdown();
            logger.info("close thread poll {} [{}]", entry.getKey(), executorService.isTerminated());
            try {
                executorService.awaitTermination(10, TimeUnit.SECONDS);
            }catch (InterruptedException e) {
                logger.error("close thread poll fail {}",e.getMessage());
                executorService.shutdown();
            }
        });
    }

    private static ExecutorService createThreadPool(String threadPrefixName, Boolean daemon) {
        BlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue<>(QUEUE_CAPACITY);
        ThreadFactory factory = createThreadFactory(threadPrefixName, daemon);
        return new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE,KEEP_ALIVE_TIME,TimeUnit.MINUTES, blockingQueue, factory);
    }

    /**
     * 创建线程工厂
     * @param threadPrefixName 线程前缀名字
     * @param daemon 是否为守护线程
     */
    private static ThreadFactory createThreadFactory(String threadPrefixName, Boolean daemon) {
        if (daemon == null) {
            return new ThreadFactoryBuilder().setNameFormat(threadPrefixName + "-%d").build();
        }else {
            return new ThreadFactoryBuilder().setNameFormat(threadPrefixName + "-%d").setDaemon(daemon).build();
        }
    }

}
