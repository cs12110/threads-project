package com.test;

import com.pkg.threads.util.ThreadUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author huapeng.huang
 * @version V1.0
 * @since 2020-09-14 16:35
 */
public class ThreadPoolRejectPolicyTest {

    @Data
    @AllArgsConstructor
    public static class ForestGam implements Runnable {

        private String threadName;

        @Override
        public void run() {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
                System.out.println(
                    sdf.format(new Date()) + "\t" + Thread.currentThread().getId() + "\t" + threadName + " start");
                Thread.sleep(10000);
                System.out.println(
                    sdf.format(new Date()) + "\t" + Thread.currentThread().getId() + "\t" + threadName + " is done");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        ThreadFactory policy = ThreadUtil.buildFactory("policy");
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(1), policy, new ThreadPoolExecutor.CallerRunsPolicy());

        for (int index = 0; index < 10; index++) {
            threadPoolExecutor.submit(new ForestGam("thr" + index));
        }

    }
}
