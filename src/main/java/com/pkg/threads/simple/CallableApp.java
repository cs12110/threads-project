package com.pkg.threads.simple;

import com.pkg.threads.util.LogUtil;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * @author huapeng.huang
 * @version V1.0
 * @since 2020-08-05 10:44
 */
public class CallableApp {

    private static LogUtil logUtil = new LogUtil(SimpleCallable.class);

    public static class SimpleCallable implements Callable<Object> {

        @Override
        public Object call() throws Exception {
            return "返回值";
        }
    }

    public static void main(String[] args) {
        SimpleCallable callable = new SimpleCallable();

        FutureTask<Object> futureTask = new FutureTask<>(callable);
        new Thread(futureTask).start();

        try {
            Object o = futureTask.get();
            logUtil.info("Value:{}", o);
        } catch (Exception e) {
            logUtil.error(e.getMessage());
        }
    }
}
