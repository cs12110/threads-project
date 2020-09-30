package com.test;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author huapeng.huang
 * @version V1.0
 * @since 2020-09-16 09:41
 */
public class Num2CnUtil {

    private static String[] s1 = { "零", "一", "二", "三", "四", "五", "六", "七", "八", "九" };
    private static String[] s2 = { "十", "百", "千", "万", "十", "百", "千", "亿", "十", "百", "千" };

    public static void main(String[] args) {
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE, -1);
        instance.set(Calendar.HOUR_OF_DAY, 0);
        instance.set(Calendar.MINUTE, 0);
        instance.set(Calendar.SECOND, 0);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        System.out.println(sdf.format(instance.getTime()));
    }

    public static String num2Cn(String string) {
        if ("0".equals(string)) {
            return s1[0];
        }
        String result = "";
        StringBuilder builder = new StringBuilder();
        int n = string.length();
        for (int i = 0; i < n; i++) {
            int num = string.charAt(i) - '0';
            if (i != n - 1 && num != 0) {
                builder.append(s1[num]).append(s2[n - 2 - i]);
            } else {
                builder.append(s1[num]);
            }
        }

        result = builder.toString();
        if (result.matches("一十.?")) {
            result = result.substring(1);
        }
        if (result.matches(".*零+")) {
            result = result.substring(0, result.indexOf("零"));
        }
        return result;
    }
}
