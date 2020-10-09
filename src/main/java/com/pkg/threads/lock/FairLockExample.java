package com.pkg.threads.lock;

import com.pkg.threads.util.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import lombok.AllArgsConstructor;

/**
 * @author huapeng.huang
 * @version V1.0
 * @since 2020-10-09 08:57
 */
public class FairLockExample {

    private static LogUtil logUtil = LogUtil.get(FairLockExample.class);

    @AllArgsConstructor
    private static class LockRunnable implements Runnable {

        private String threadName;
        private ReentrantLock lock;

        @Override
        public void run() {
            logUtil.info("Function[run]thread:{} start", threadName);
            lock.lock();
            try {
                logUtil.info("Function[run]thread:{} get the lock", threadName);

                // sleep 500ms
                Thread.sleep(500);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        // 设置是否为公平式锁,为true时为公平式锁
        ReentrantLock lock = new ReentrantLock(Boolean.FALSE);

        int threadNum = 10;
        List<Thread> list = new ArrayList<>(threadNum);
        for (int index = 0; index < threadNum; index++) {
            Thread thread = new Thread(new LockRunnable("t" + index, lock));
            list.add(thread);
        }

        for (Thread t : list) {
            t.start();
        }
    }

}
