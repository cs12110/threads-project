# 多线程演义

多线程演义第一章: 起初图灵创建了计算机.CPU 是单线程的,内存虚弱.代码的灵运行在打孔纸带上.

文档演示代码 [github link](https://github.com/cs12110/threads-project)

---

### 1. 线程

多线程演义第二章: ta 那时候还太年轻,不知道所有 Java 里面的线程,早已在暗中标好了价格.

#### 1.1 Thread

```java
public class ThreadApp {

    private static LogUtil logUtil = new LogUtil(SimpleThread.class);

    public static class SimpleThread extends Thread {
        @Override
        public void run() {
            logUtil.info("执行完成");
        }
    }

    public static void main(String[] args) {
        SimpleThread simpleThread = new SimpleThread();
        simpleThread.start();
    }
}
```

测试结果

```java
2020-08-06 15:27:08,558	[info]	com.pkg.threads.simple.ThreadApp$SimpleThread	执行完成
```

#### 1.2 Runnable

```java
public class RunnableApp {

    private static LogUtil logUtil = new LogUtil(SimpleRunnable.class);

    public static class SimpleRunnable implements Runnable {
        private String threadName;

        public SimpleRunnable(String threadName) {
            this.threadName = threadName;
        }

        @Override
        public void run() {
            try {
                logUtil.info(threadName + "开始执行");
                Thread.sleep(1000);
                logUtil.info(threadName + "执行完成");
            } catch (Exception e) {
                logUtil.error(e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        for (int index = 0; index < 2; index++) {
            String threadName = "simple-thread" + index;
            SimpleRunnable runnable = new SimpleRunnable(threadName);
            new Thread(runnable).start();
        }
    }
}
```

测试结果

```java
2020-08-06 15:27:32,051	[info]	com.pkg.threads.simple.RunnableApp$SimpleRunnable	simple-thread1开始执行
2020-08-06 15:27:32,051	[info]	com.pkg.threads.simple.RunnableApp$SimpleRunnable	simple-thread0开始执行
2020-08-06 15:27:33,054	[info]	com.pkg.threads.simple.RunnableApp$SimpleRunnable	simple-thread1执行完成
2020-08-06 15:27:33,054	[info]	com.pkg.threads.simple.RunnableApp$SimpleRunnable	simple-thread0执行完成
```

#### 1.3 Callable

```java
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
```

测试结果

```java
2020-08-06 15:27:54,362	[info]	com.pkg.threads.simple.CallableApp$SimpleCallable	Value:返回值
```

#### 1.4 小节总结

多线程使用准则第一条: `如果能用一条线程解决,就千万别用两条线程.`

线程数的大小设计

• 如果是 CPU 密集型应用,则线程池大小设置为 `CPU 个数+1`

• 如果是 IO 密集型应用,则线程池大小设置为 `2*CPU 个数+1`

Q: <u>如果在 Thread 和 Runnable 两种方式里面实现多线程?采用哪种比较好?</u>

A:

---

### 2. 线程池

如果频繁的创建线程和销毁会导致资源的损耗,所以使用池的方式来降低资源损耗.

FBI Warning:`线程池不会自动关闭.`

#### 2.1 ThreadPoolExecutor

```java
public static ThreadPoolExecutor build() {
    // 线程池核心数量
    int coreSize = 2;
    // 线程池最大数量
    int maxSize = 2;
    // 空余线程被回收时间单位
    int keepAliveTime = 0;
    TimeUnit timeUnit = TimeUnit.SECONDS;

    // 队列???
    ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(10);
    // LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();
    NamingThreadFactory threadFactory = new NamingThreadFactory("tom&jerry");

    // 构建线程池
    return new ThreadPoolExecutor(coreSize, maxSize, keepAliveTime, timeUnit, queue, threadFactory);
}
```

- coreSize: 线程池核心线程数
- maxSize: 线程池最大线程数
- keepAliveTime 和 unit: 两个组合使用,表示空闲的线程(只回收>coresize 的线程)在多少时间后被回收,最多缩减为 coresize 数量
- queue: 等待队列的大小

陆续创建核心线程数大小的线程 -> 消费不过了,放到等待队列里面 -> 队列满了 -> 扩充线程池线程数,最大为最大线程数 -> 队列满了(有边界的队列才会满 `orz`),池已经扩充到最大,还消费不过来-> 默认采取拒绝策略.

策略

- CallerRunsPolicy:
- AbortPolicy: 任务丢弃,抛出异常.
- DiscardPolicy: 任务直接抛弃,不会抛异常也不会执行.
- DiscardOldestPolicy: 抛弃任务队列中最旧的任务,新任务添加进去.

关于线程池的命名: **便于监控查看.**

![](imgs/threadfactory.png)

#### 2.2 Executors

我们不生产代码,我们只是代码的搬运工.jpg

**单线程线程池**

```java
public class ExecutorsApp {

    public static void main(String[] args) {
        /*
         * 创建单个线程的线程池,coreSize=1,maxSize=1,queue=LinkedBlockingQueue
         */
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        ThreadUtil.shutdown(singleThreadExecutor);
    }
}
```

实现原理:

```java
public static ExecutorService newSingleThreadExecutor() {
    return new FinalizableDelegatedExecutorService
        (new ThreadPoolExecutor(1, 1,
                                0L, TimeUnit.MILLISECONDS,
                                new LinkedBlockingQueue<Runnable>()));
}
```

**固定线程池**

```java
public class ExecutorsApp {

    public static void main(String[] args) {
        /*
         * 创建固定线程的线程池,coreSize=2,maxSize=2,queue=LinkedBlockingQueue
         */
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(2);
        ThreadUtil.shutdown(fixedThreadPool);
    }
}
```

实现原理:

```java
public static ExecutorService newFixedThreadPool(int nThreads) {
        return new ThreadPoolExecutor(nThreads, nThreads,
                                      0L, TimeUnit.MILLISECONDS,
                                      new LinkedBlockingQueue<Runnable>());
}
```

**缓存线程池**

```java
public class ExecutorsApp {

    public static void main(String[] args) {
        /*
         * 创建一直扩展线程的线程池,coreSize=0,maxSize=Integer.MAX_VALUE,queue=SynchronousQueue
         */
        ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();
        ThreadUtil.shutdown(newCachedThreadPool);
    }
}
```

实现原理:

```java
public static ExecutorService newCachedThreadPool() {
    return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                                    60L, TimeUnit.SECONDS,
                                    new SynchronousQueue<Runnable>());
}
```

**结论**: <u>`java.util.concurrent.Executors`底层是`ThreadPoolExecutor`构建的,所以掌握`ThreadPoolExecutor`至关重要.</u>

#### 2.3 扩展阅读

[线程池执行原理 link](https://mr3306.top/docs/#/javase/java-threadpool?id=_7-%e7%ba%bf%e7%a8%8b%e6%b1%a0%e5%ae%9e%e7%8e%b0%e5%8e%9f%e7%90%86)

---

### 3. JMM

#### 3.1 基础知识

- 主内存

主要存储的是 Java 实例对象,所有线程创建的实例对象都存放在主内存中,不管该实例对象是成员变量还是方法中的本地变量(也称局部变量),当然也包括了共享的类信息,常量,静态变量.由于是共享数据区域,多条线程对同一个变量进行访问可能会发现线程安全问题.

- 工作内存

主要存储当前方法的所有本地变量信息(工作内存中存储着主内存中的变量副本拷贝),每个线程只能访问自己的工作内存,即线程中的本地变量对其它线程是不可见的,就算是两个线程执行的是同一段代码,它们也会各自在自己的工作内存中创建属于当前线程的本地变量,当然也包括了字节码行号指示器,相关 Native 方法的信息.注意由于工作内存是每个线程的私有数据,线程间无法相互访问工作内存,因此存储在工作内存的数据不存在线程安全问题.

![](imgs/jmm.jpg)

#### 3.2 JMM

在线程里面所有操作的数据来自该工作内存,操作完成后再刷回主存里面去.

Q: 在执行方法的时候,有哪些数据存放到工作空间里面呀?

A: 根据虚拟机规范.对于一个实例对象中的成员方法而言.如果方法中包含本地变量是基本数据类型(boolean,byte,short,char,int,long,float,double).将直接存储在工作内存的帧栈结构中.但倘若本地变量是引用类型.那么该变量的引用会存储在功能内存的帧栈中.而对象实例将存储在主内存(共享数据区域.堆)中.但对于实例对象的成员变量.不管它是基本数据类型或者包装类型(Integer,Double 等)还是引用类型.都会被存储到堆区.至于 static 变量以及类本身相关信息将会存储在主内存中.需要注意的是.在主内存中的实例对象可以被多线程共享.倘若两个线程同时调用了同一个对象的同一个方法.那么两条线程会将要操作的数据拷贝一份到自己的工作内存中.执行完成操作后才刷新到主内存.[link](https://blog.csdn.net/javazejian/article/details/72772461)

JMM 是围绕原子性,有序性,可见性展开的.

- 原子性指的是一个操作是不可中断的,即使是在多线程环境下,一个操作一旦开始就不会被其他线程影响.
- 有序性指程序执行的顺序按照代码的先后顺序执行.[link](https://www.cnblogs.com/yeyang/p/13576636.html)
- 可见性指的是当一个线程修改了某个共享变量的值,其他线程能否马上得知修改的值.

#### 3.3 Volatile&Synchnorized&Lock

- `Volatitle`: 通过内存和禁止指令重排,实现共享变量的可见性,不具备原子性.
- `Synchnorized`: 能保证可见性和原子性.
- `Lock`: `Synchorized`的升级版本.

##### Volatile

内存栅栏的含义和例子说明.

##### Synchnorized

##### Lock

公平式锁与非公平式锁.

##### CountdownLatch&Join

```java

```

#### 3.4 CAS

- 悲观锁: 线程一旦得到锁,其他需要锁的线程挂起等待.

- 乐观锁: 每次不加锁而是假设没有冲突而去完成某项操作,如果冲突失败就重试,直到成功为止.

无锁的实现, compare and swap(对比和转换).CAS 机制当中使用了 3 个基本操作数:`内存地址 V`,`旧的预期值 A`,`新值 B`.

Q: 那么使用 cas 相关类有什么需要注意的呀?

A:

---

### 4. 常见案例

#### 4.1 线程池统计

场景: 用户触发请求,使用线程池进行异步业务逻辑处理.

```java
// 请求次数
int requestTimes = 0;
for (; requestTimes < 2; requestTimes++) {
    ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 2, 0, TimeUnit.SECONDS,
        new ArrayBlockingQueue<>(10), new NightWatcherThreadFactory("counter" + requestTimes + "-"));
    // 异步执行业务1
    executor.submit(new WorkRunnable());
    // 异步执行业务2
    executor.submit(new WorkRunnable());
}

// 返回
logUtil.info("Main method is done");
```

Q: 这个会出现什么问题?要怎么优化?

A:

#### 4.2 SimpleDateFormat

```java
public class SdfExample {

    private static LogUtil logUtil = LogUtil.get(SdfExample.class);
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
```

#### 4.3 ThreadLocal

调用链路的日志打印,例如 logback 的 mdc.

```java
public class ThreadLocalExample {

    private static LogUtil logUtil = LogUtil.get(ThreadLocalExample.class);
    private static ThreadLocal<String> threadLocalCache = new ThreadLocal<>();

    public static void set(String value) {
        threadLocalCache.set(value);
    }

    public static String get() {
        return threadLocalCache.get();
    }

    public static void remove() {
        threadLocalCache.remove();
    }

    public static void main(String[] args) {
        set("haiyan");
        method1();
        set("value2");
        method2();
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
```

Q: 上面的代码有什么问题?该如何改进?

Q: 多线程的情况,例如 spring 的 event 那种还能获取出来吗?

#### 4.4 更多

如想了解更多案例,可以参考: [fucking-java-concurrency github link](https://github.com/oldratlee/fucking-java-concurrency)

---

### 5. 致谢

多线程演义第五章: <u>多年以后,我站在施工队的砖头前,准会想起给各位大佬讲多线程演义的那个遥远的午后.</u>

**参考文档**

a. [全面理解 Java 内存模型(JMM)及 volatile 关键字 link](https://blog.csdn.net/javazejian/article/details/72772461)

b. [深入理解 Java 并发之 synchronized 实现原理 link](https://blog.csdn.net/javazejian/article/details/72828483)

c. [ThreadLocal 原理 link](https://mr3306.top/docs/#/javase/java-threadlocal)

d. [线程池执行原理 link](https://mr3306.top/docs/#/javase/java-threadpool)
