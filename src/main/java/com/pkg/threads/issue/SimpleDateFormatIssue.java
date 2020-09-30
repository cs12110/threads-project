package com.pkg.threads.issue;

import com.pkg.threads.util.LogUtil;
import com.pkg.threads.util.ThreadUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author huapeng.huang
 * @version V1.0
 * @since 2020-08-13 10:00
 */
public class SimpleDateFormatIssue {

    private static LogUtil logUtil = LogUtil.get(SimpleDateFormatIssue.class);
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static ThreadLocal<SimpleDateFormat> threadLocal = ThreadLocal
        .withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

    /**
     * 测试SimpleDateFormat在多线程的情况下出现转换问题
     * <p>
     * 原因: 因为SimpleDateFormat设置为static,多个线程同时共用ta设置里面的成员属性:Calendar
     */
    public static class BugRunner implements Runnable {
        @Override
        public void run() {
            try {
                String dateStr = "1984-03-06 12:00:00";
                Date date = sdf.parse(dateStr);

                // ThreadLocal
                //                SimpleDateFormat simpleDateFormat = threadLocal.get();
                //                Date date = simpleDateFormat.parse(dateStr);

                logUtil
                    .info("Function[run] thread:{},date value:{}", Thread.currentThread().getName(), sdf.format(date));
            } catch (Exception e) {
                logUtil.error("Function[run] thread:" + Thread.currentThread().getName(), e);
            }
        }
    }

    public static void main(String[] args) {
        ThreadPoolExecutor executor = ThreadUtil.fixed("sdf-pool-", 10);

        for (int index = 0; index < 10; index++) {
            executor.submit(new BugRunner());
        }
        ThreadUtil.shutdown(executor);
    }
}
