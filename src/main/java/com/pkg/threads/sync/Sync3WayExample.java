package com.pkg.threads.sync;

import com.pkg.threads.util.LogUtil;
import com.pkg.threads.util.ThreadUtil;

import java.util.concurrent.ThreadPoolExecutor;

import lombok.AllArgsConstructor;

/**
 * @author huapeng.huang
 * @version V1.0
 * @since 2020-08-07 14:17
 */
public class Sync3WayExample {

    private static LogUtil logUtil = LogUtil.get(Sync3WayExample.class);

    public static synchronized void syncWithMethod() {
        try {
            logUtil.info("Function[syncWithMethod] thread:{} start", ThreadUtil.getCurrentThreadName());

            Thread.sleep(1000);

            logUtil.info("Function[syncWithMethod] thread:{} done", ThreadUtil.getCurrentThreadName());
        } catch (Exception e) {
            logUtil.error("Function[syncWithMethod] thread:" + ThreadUtil.getCurrentThreadName(), e);
        }
    }

    public static void syncWithClass() {
        synchronized (Sync3WayExample.class) {
            try {
                logUtil.info("Function[syncWithClass] thread:{} start", ThreadUtil.getCurrentThreadName());

                Thread.sleep(1000);

                logUtil.info("Function[syncWithClass] thread:{} done", ThreadUtil.getCurrentThreadName());
            } catch (Exception e) {
                logUtil.error("Function[syncWithClass] thread:" + ThreadUtil.getCurrentThreadName(), e);
            }
        }
    }

    public static void syncWithObj(Object syncObject) {
        synchronized (syncObject) {
            try {
                logUtil.info("Function[syncWithObj] thread:{} start", ThreadUtil.getCurrentThreadName());

                Thread.sleep(1000);

                logUtil.info("Function[syncWithObj] thread:{} done", ThreadUtil.getCurrentThreadName());
            } catch (Exception e) {
                logUtil.error("Function[syncWithObj] thread:" + ThreadUtil.getCurrentThreadName(), e);
            }
        }
    }

    static class SyncMethodRunner implements Runnable {
        @Override
        public void run() {
            syncWithMethod();
        }
    }

    static class SyncClassRunner implements Runnable {
        @Override
        public void run() {
            syncWithClass();
        }
    }

    @AllArgsConstructor
    static class SyncObjectRunner implements Runnable {
        private Object syncObject;

        @Override
        public void run() {
            syncWithObj(syncObject);
        }
    }

    public static void main(String[] args) {

        ThreadPoolExecutor pool = ThreadUtil.fixed("sync", 2);

        // sync method
        //        pool.submit(new SyncMethodRunner());
        //        pool.submit(new SyncMethodRunner());

        // sync class
        //        pool.submit(new SyncClassRunner());
        //        pool.submit(new SyncClassRunner());

        // sync object
        //        Object syncObject = new Object();
        //        pool.submit(new SyncObjectRunner(syncObject));
        //        pool.submit(new SyncObjectRunner(syncObject));
        pool.submit(new SyncObjectRunner(new Object()));
        pool.submit(new SyncObjectRunner(new Object()));

        ThreadUtil.shutdown(pool);
    }

}
