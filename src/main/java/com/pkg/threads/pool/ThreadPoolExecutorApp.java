package com.pkg.threads.pool;

import com.pkg.threads.util.LogUtil;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author huapeng.huang
 * @version V1.0
 * @since 2020-08-05 11:19
 */
public class ThreadPoolExecutorApp {

    private static LogUtil logUtil = LogUtil.get(ThreadPoolExecutorApp.class);

    public static class NightWatcherThreadFactory implements ThreadFactory {

        private AtomicInteger threadCounter = new AtomicInteger(1);
        private String prefix;

        public NightWatcherThreadFactory(String prefix) {
            this.prefix = prefix;
        }

        @Override
        public Thread newThread(Runnable r) {
            String threadName = prefix + threadCounter.getAndDecrement();
            return new Thread(r, threadName);
        }
    }

    public static class WorkRunnable implements Runnable {

        @Override
        public void run() {
            // do something
        }
    }

    public static void main(String[] args) {
        for (int requestTimes = 0; requestTimes < 2; requestTimes++) {
            ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 2, 0, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(10), new NightWatcherThreadFactory("counter" + requestTimes + "-"));
            executor.submit(new WorkRunnable());
            executor.submit(new WorkRunnable());
        }
        logUtil.info("Main method is done");
    }
}
