package com.test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author huapeng.huang
 * @version V1.0
 * @since 2020-09-30 14:48
 */
public class Parrel {

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();

        list.parallelStream().map(s->s+"1").collect(Collectors.toList());
    }
}
