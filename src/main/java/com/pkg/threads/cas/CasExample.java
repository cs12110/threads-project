package com.pkg.threads.cas;

import com.pkg.threads.util.LogUtil;
import com.pkg.threads.util.ThreadUtil;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.AllArgsConstructor;

/**
 * @author huapeng.huang
 * @version V1.0
 * @since 2020-09-30 15:41
 */
public class CasExample {

    private static LogUtil logUtil = LogUtil.get(CasExample.class);

    @AllArgsConstructor
    private static class CasRunnable implements Runnable {

        private AtomicInteger counter;

        @Override
        public void run() {
            int num = counter.getAndIncrement();
            logUtil.info("Function[run] thread:{} num:{}", Thread.currentThread().getId(), num);
        }
    }

    public static void main(String[] args) {

        AtomicInteger counter = new AtomicInteger(1);

        ThreadPoolExecutor executor = ThreadUtil.fixed("dog", 2);

        executor.submit(new CasRunnable(counter));
        executor.submit(new CasRunnable(counter));
        executor.submit(new CasRunnable(counter));
        executor.submit(new CasRunnable(counter));
    }
}
