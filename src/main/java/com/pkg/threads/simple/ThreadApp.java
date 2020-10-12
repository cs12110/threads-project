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
            try {
                logUtil.info("Function[run] 吉米.巴特勒拿到了x分y篮板z助攻");
                Thread.sleep(2000);
                logUtil.info("Function[run] xx总冠军");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        logUtil.info("Function[main] start up");
        SimpleThread simpleThread = new SimpleThread();
        simpleThread.start();
        logUtil.info("Function[main] end");
    }
}
