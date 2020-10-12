package com.test;

import com.pkg.threads.util.LogUtil;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import lombok.AllArgsConstructor;

/**
 * @author huapeng.huang
 * @version V1.0
 * @since 2020-10-12 11:43
 */
public class ThreadPoolReduceWatcherTest {

    private static LogUtil logUtil = LogUtil.get(ThreadPoolReduceWatcherTest.class);

    @AllArgsConstructor
    private static class WatneyRunnable implements Runnable {

        private String threadName;

        @Override
        public void run() {
            try {
                logUtil.info("Function[run]thread:{} start up", threadName);
                Thread.sleep(20000);
                logUtil.info("Function[run]thread:{} end", threadName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {

        ThreadPoolExecutor pool = new ThreadPoolExecutor(2, 4, 0, TimeUnit.SECONDS, new LinkedBlockingQueue<>(2));

        for (int index = 0; index < 6; index++) {
            String threadName = "t" + index;

            pool.submit(new WatneyRunnable(threadName));
        }
    }
}
