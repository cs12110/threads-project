package com.pkg.threads.simple;

import com.pkg.threads.util.LogUtil;

/**
 * @author huapeng.huang
 * @version V1.0
 * @since 2020-08-05 10:44
 */
public class ThreadApp {

    private static LogUtil logUtil = new LogUtil(SimpleThread.class);

    public static class SimpleThread extends Thread {
        @Override
        public void run() {
            logUtil.info("执行完成");
        }
    }

    public static void main(String[] args) {
        SimpleThread simpleThread = new SimpleThread();
        simpleThread.start();
    }
}
