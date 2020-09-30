package com.pkg.threads.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * @author huapeng.huang
 * @version V1.0
 * @since 2020-08-05 10:46
 */
public class LogUtil {

    public static LogUtil get(Class<?> clazz) {
        return new LogUtil(clazz);
    }

    private Class<?> targetClass;

    public LogUtil(Class<?> targetClass) {
        this.targetClass = targetClass;
    }

    public void debug(String body, Object... args) {

        String debug = dealWithLog("debug", body, args);
        display(debug);
    }

    public void info(String body, Object... args) {
        String info = dealWithLog("info", body, args);
        display(info);
    }

    public void error(String body, Object... args) {
        String error = dealWithLog("error", body, args);
        display(error);
    }

    public void error(String msg, Throwable throwable) {
        String errMessage = throwable.toString();
        String log = dealWithLog("error", msg + "\t{}", errMessage);

        display(log, Boolean.TRUE);
    }

    private static void display(String log) {
        display(log, Boolean.FALSE);
    }

    private static void display(String log, boolean isErr) {
        if (isErr) {
            System.err.println(log);
        } else {
            System.out.println(log);
        }
    }

    private String dealWithLog(String logLevel, String body, Object... args) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
        StringBuilder log = new StringBuilder();
        log.append(sdf.format(new Date()));
        log.append("\t");
        log.append("[").append(logLevel).append("]");
        log.append("\t");
        log.append(simplifyClassName(targetClass));
        log.append("\t");

        if (null != body && !"".equals(body)) {
            if (null != args && args.length > 0) {
                for (Object arg : args) {
                    body = body.replaceFirst("\\{\\}", String.valueOf(arg));
                }
            }
        }

        log.append(body);

        return log.toString();
    }

    private String simplifyClassName(Class<?> clazz) {
        if (Objects.isNull(clazz)) {
            return "N/A";
        }

        String className = clazz.getName();
        int lastDotIndex = className.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return className;
        }
        StringBuilder shortClassName = new StringBuilder(className.length());
        int start = 0;
        while (true) {
            shortClassName.append(className.charAt(start));
            int next = className.indexOf('.', start);
            if (next == lastDotIndex) {
                break;
            }
            start = next + 1;
            shortClassName.append('.');
        }
        shortClassName.append(className.substring(lastDotIndex));
        return shortClassName.toString();
    }
}
