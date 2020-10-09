package com.pkg.threads.issue;

import com.pkg.threads.util.LogUtil;
import com.pkg.threads.util.ThreadUtil;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author huapeng.huang
 * @version V1.0
 * @since 2020-09-02 10:53
 */
public class ThreadLocalIssue {

    private static LogUtil logUtil = LogUtil.get(ThreadLocalIssue.class);
    private static ThreadLocal<String> threadLocalCache = new ThreadLocal<>();
    //private static InheritableThreadLocal<String> threadLocalCache = new InheritableThreadLocal<>();

    public static void set(String value) {
        threadLocalCache.set(value);
    }

    public static String get() {
        return threadLocalCache.get();
    }

    public static void remove() {
        threadLocalCache.remove();
    }

    public static class ThreadValueRunner implements Runnable {

        @Override
        public void run() {
            logUtil.info("Function[run] cache value:{}", get());
        }
    }

    public static void main(String[] args) {
        ThreadPoolExecutor pool = ThreadUtil.fixed("local-pool", 2);

        set("haiyan");
        method1();
        set("value2");
        method2();

        pool.submit(new ThreadValueRunner());
        pool.submit(new ThreadValueRunner());
    }

    public static void method1() {
        logUtil.info("Function[method1] cache value:{}", get());
    }

    public static void method2() {
        logUtil.info("Function[method2] cache value:{}", get());
    }

    public static void method3() {
        logUtil.info("Function[method3] after remove:{}", get());
    }
}
