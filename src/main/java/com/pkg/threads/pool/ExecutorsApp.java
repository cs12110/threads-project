package com.pkg.threads.pool;

import com.pkg.threads.util.ThreadUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 常用的创建线程池方法
 *
 * @author huapeng.huang
 * @version V1.0
 * @since 2020-08-05 11:19
 */
public class ExecutorsApp {

    public static void main(String[] args) {

        /*
         * 创建单个线程的线程池,coreSize=1,maxSize=1,queue=LinkedBlockingQueue
         */
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        ThreadUtil.shutdown(singleThreadExecutor);

        /*
         * 创建固定线程的线程池,coreSize=2,maxSize=2,queue=LinkedBlockingQueue
         */
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(2);
        ThreadUtil.shutdown(fixedThreadPool);

        /*
         * 创建一直扩展线程的线程池,coreSize=0,maxSize=Integer.MAX_VALUE,queue=SynchronousQueue
         */
        ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();
        ThreadUtil.shutdown(newCachedThreadPool);
    }
}
