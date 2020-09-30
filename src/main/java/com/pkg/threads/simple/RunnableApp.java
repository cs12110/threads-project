package com.pkg.threads.simple;

import com.pkg.threads.util.LogUtil;

/**
 * @author huapeng.huang
 * @version V1.0
 * @since 2020-08-05 10:44
 */
public class RunnableApp {

    private static LogUtil logUtil = new LogUtil(SimpleRunnable.class);

    public static class SimpleRunnable implements Runnable {
        private String threadName;

        public SimpleRunnable(String threadName) {
            this.threadName = threadName;
        }

        @Override
        public void run() {
            try {
                logUtil.info(threadName + "开始执行");
                Thread.sleep(1000);
                logUtil.info(threadName + "执行完成");
            } catch (Exception e) {
                logUtil.error(e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        for (int index = 0; index < 2; index++) {
            String threadName = "simple-thread" + index;
            SimpleRunnable runnable = new SimpleRunnable(threadName);
            new Thread(runnable).start();
        }
    }
}
