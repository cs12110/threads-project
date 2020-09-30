package com.pkg.threads.util;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author huapeng.huang
 * @version V1.0
 * @since 2020-08-06 15:07
 */
public class ThreadUtil {

    /**
     * 自定义ThreadFactory
     */
    public static class NamingThreadFactory implements ThreadFactory {

        private String prefix;
        private AtomicInteger threadCounter = new AtomicInteger(1);

        public NamingThreadFactory(String prefix) {
            this.prefix = prefix;
        }

        @Override
        public Thread newThread(Runnable r) {
            String threadName = prefix + threadCounter.getAndIncrement();
            return new Thread(r, threadName);
        }
    }

    /**
     * 构建thread factory
     *
     * @param name name
     * @return ThreadFactory
     */
    public static ThreadFactory buildFactory(String name) {
        return new NamingThreadFactory(name);
    }

    /**
     * 创建线程池
     *
     * @param poolName 线程池名称
     * @param size     size
     * @return ThreadPoolExecutor
     */
    public static ThreadPoolExecutor fixed(String poolName, int size) {
        return build(poolName, size, size);
    }

    /**
     * 创建线程池
     *
     * @param prefix   前缀
     * @param coreSize coreSize
     * @param maxSize  maxSize
     * @return ThreadPoolExecutor
     */
    public static ThreadPoolExecutor build(String prefix, int coreSize, int maxSize) {
        // 空余线程被回收时间单位
        int keepAliveTime = 0;
        TimeUnit timeUnit = TimeUnit.SECONDS;

        // 边界队列
        //ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(10);
        LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();
        NamingThreadFactory threadFactory = new NamingThreadFactory(prefix);

        // 构建线程池
        return new ThreadPoolExecutor(coreSize, maxSize, keepAliveTime, timeUnit, queue, threadFactory);
    }

    /**
     * 关闭线程池
     *
     * @param executor ExecutorService
     */
    public static void shutdown(ExecutorService executor) {
        if (Objects.isNull(executor)) {
            return;
        }

        if (executor.isShutdown()) {
            return;
        }

        executor.shutdown();
    }

    /**
     * 获取当前线程Id
     *
     * @return String
     */
    public static Long getCurrentThreadId() {
        return Thread.currentThread().getId();
    }

    /**
     * 获取当前线程名称
     *
     * @return String
     */
    public static String getCurrentThreadName() {
        return Thread.currentThread().getName();
    }
}
