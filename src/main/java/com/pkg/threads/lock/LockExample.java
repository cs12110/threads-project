package com.pkg.threads.lock;

import com.pkg.threads.util.LogUtil;
import com.pkg.threads.util.ThreadUtil;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.ReentrantLock;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

/**
 * @author huapeng.huang
 * @version V1.0
 * @since 2020-08-07 14:17
 */
public class LockExample {

    private static LogUtil logUtil = LogUtil.get(LockExample.class);

    @Data
    @Setter
    private static class IncrCounter {
        private int num = 0;
        private ReentrantLock reentrantLock;

        public void incr() {
            reentrantLock.lock();
            try {
                num = num + 1;
            } finally {
                reentrantLock.unlock();
            }
        }

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

        ReentrantLock reentrantLock = new ReentrantLock();
        IncrCounter incrCounter = new IncrCounter();
        incrCounter.setReentrantLock(reentrantLock);

        ThreadPoolExecutor counter = ThreadUtil.fixed("sync", threadNum);

        for (int i = 0; i < threadNum; i++) {
            counter.submit(new MyRunnable(incrCounter));
        }

        try {
            // 保证线程全部执行完成
            counter.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }

        logUtil.info("Function[main] after shutdown pool, num:{}", incrCounter.getNum());
    }

}
