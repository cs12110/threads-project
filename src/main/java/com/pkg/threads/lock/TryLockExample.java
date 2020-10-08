package com.pkg.threads.lock;

import com.pkg.threads.util.LogUtil;

import java.util.concurrent.locks.ReentrantLock;

import lombok.AllArgsConstructor;

/**
 * @author huapeng.huang
 * @version V1.0
 * @since 2020-08-07 14:17
 */
public class TryLockExample {

    private static LogUtil logUtil = LogUtil.get(TryLockExample.class);

    @AllArgsConstructor
    private static class MyRunnable implements Runnable {

        private String threadName;
        private ReentrantLock lock;

        @Override
        public void run() {
            boolean tryLock = false;
            try {
                tryLock = lock.tryLock();
                //tryLock = lock.tryLock(2, TimeUnit.SECONDS);
                logUtil.info("Function[run] thread:{} get lock:{}", threadName, tryLock);
                if (tryLock) {
                    Thread.sleep(5000);
                }
                logUtil.info("Function[run] thread:{} all done", threadName);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (tryLock) {
                    lock.unlock();
                    logUtil.info("Function[run] thread:{} release lock", threadName);
                }
            }
        }
    }

    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();

        new Thread(new MyRunnable("t1", lock)).start();
        new Thread(new MyRunnable("t2", lock)).start();
    }
}
