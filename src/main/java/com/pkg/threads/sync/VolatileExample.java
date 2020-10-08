package com.pkg.threads.sync;

import com.alibaba.fastjson.JSON;
import com.pkg.threads.util.LogUtil;

import java.util.Arrays;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 模拟: 生产者生产一批数据,当生产者设置可以消费时,消费者开始消费数据
 *
 * @author huapeng.huang
 * @version V1.0
 * @since 2020-08-07 13:53
 */
public class VolatileExample {

    private static LogUtil logUtil = LogUtil.get(VolatileExample.class);

    @Data
    public static class DataStore {
        private boolean isProvideFinish;
        private List<Object> values;
    }

    @AllArgsConstructor
    public static class DataProducer implements Runnable {

        private DataStore dataStore;

        @Override
        public void run() {
            logUtil.info("Function[DataProducer.start] start");
            dataStore.setProvideFinish(Boolean.FALSE);
            try {
                Thread.sleep(500);
                dataStore.setValues(Arrays.asList("1", "2", "3"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            dataStore.setProvideFinish(Boolean.TRUE);
            logUtil.info("Function[DataProducer.start] end");
        }
    }

    @AllArgsConstructor
    public static class DataConsumer implements Runnable {
        private DataStore dataStore;

        @Override
        public void run() {
            logUtil.info("Function[DataConsumer.run] start");
            while (!dataStore.isProvideFinish) {
                // do something
            }

            logUtil.info("Function[DataConsumer.run] values:{}", JSON.toJSONString(dataStore.getValues()));
            logUtil.info("Function[DataConsumer.run] end");
        }
    }

    public static void main(String[] args) {
        DataStore store = new DataStore();
        new Thread(new DataProducer(store)).start();
        new Thread(new DataConsumer(store)).start();
    }
}
