package com.pkg.threads.example;

import com.pkg.threads.util.LogUtil;
import com.pkg.threads.util.ThreadUtil;

import java.util.concurrent.ThreadPoolExecutor;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author huapeng.huang
 * @version V1.0
 * @since 2020-08-07 14:17
 */
public class SyncExample {

    private static LogUtil logUtil = LogUtil.get(SyncExample.class);

    @Data
    private static class IncrCounter {

        private volatile int num = 0;

        public void incr() {
            num = num + 1;
        }

        //        public synchronized void incr() {
        //            num = num + 1;
        //        }

    }

    @AllArgsConstructor
    private static class MyRunnable implements Runnable {

        private IncrCounter incrCounter;

        @Override
        public void run() {
            incrCounter.incr();
            //logUtil.info("Function[run] thread:{},value:{}", Thread.currentThread().getId(), incrCounter.getNum());
        }
    }

    public static void main(String[] args) {
        int threadNum = 2000;
        ThreadPoolExecutor counter = ThreadUtil.fixed("counter", threadNum);

        IncrCounter incrCounter = new IncrCounter();

        for (int i = 0; i < threadNum; i++) {
            counter.submit(new MyRunnable(incrCounter));
        }

        try {
            counter.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }

        logUtil.info("Function[main] after shutdown pool, num:{}", incrCounter.getNum());
    }

}
