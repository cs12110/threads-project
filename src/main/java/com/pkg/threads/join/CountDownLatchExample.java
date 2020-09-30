package com.pkg.threads.join;

import com.pkg.threads.util.LogUtil;

import java.util.concurrent.CountDownLatch;

import lombok.AllArgsConstructor;

/**
 * @author huapeng.huang
 * @version V1.0
 * @since 2020-09-30 15:08
 */
public class CountDownLatchExample {

    private static LogUtil logUtil = LogUtil.get(CountDownLatchExample.class);

    @AllArgsConstructor
    private static class NoneSenseRunnable implements Runnable {

        private String threadName;
        private CountDownLatch latch;

        @Override
        public void run() {
            try {
                logUtil.info("Thread:{} start", threadName);
                Thread.sleep(1000);
                logUtil.info("Thread:{} step1", threadName);
                latch.countDown();
                Thread.sleep(1000);
                logUtil.info("Thread:{} done", threadName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {

        CountDownLatch latch = new CountDownLatch(2);

        Thread t1 = new Thread(new NoneSenseRunnable("t1", latch));
        Thread t2 = new Thread(new NoneSenseRunnable("t2", latch));

        t1.start();
        t2.start();

        try {
            latch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }

        logUtil.info("Main is done");
    }
}
