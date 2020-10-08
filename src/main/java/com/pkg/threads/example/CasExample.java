package com.pkg.threads.example;

import com.pkg.threads.issue.TooManyPoolIssue;
import com.pkg.threads.util.LogUtil;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
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

        ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 2, 0, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10),
            new TooManyPoolIssue.NightWatcherThreadFactory("dog"));

        executor.submit(new CasRunnable(counter));
        executor.submit(new CasRunnable(counter));
        executor.submit(new CasRunnable(counter));
        executor.submit(new CasRunnable(counter));
    }
}
