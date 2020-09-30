package com.pkg.threads.join;

import com.pkg.threads.util.LogUtil;

import lombok.AllArgsConstructor;

/**
 * @author huapeng.huang
 * @version V1.0
 * @since 2020-09-30 15:08
 */
public class JoinExample {

    private static LogUtil logUtil = LogUtil.get(JoinExample.class);

    @AllArgsConstructor
    private static class JoinRunnable implements Runnable {

        private String threadName;

        @Override
        public void run() {
            try {
                logUtil.info("Thread:{} start", threadName);
                Thread.sleep(1000);
                logUtil.info("Thread:{} step1", threadName);
                Thread.sleep(1000);
                logUtil.info("Thread:{} done", threadName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Thread t1 = new Thread(new JoinRunnable("t1"));
        Thread t2 = new Thread(new JoinRunnable("t2"));

        try {
            t1.start();
            t1.join();

            t2.start();
            t2.join();
        } catch (Exception e) {
            e.printStackTrace();
        }

        logUtil.info("Main is done");
    }
}
