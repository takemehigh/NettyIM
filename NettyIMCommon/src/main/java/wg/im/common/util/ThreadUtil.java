package wg.im.common.util;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author gw
 * @version 1.0
 * @description: TODO
 * @date 2023/4/10 2:25 下午
 */
@Slf4j
public class ThreadUtil {

    //获取核数
    public static final int CORE_COUNT = Runtime.getRuntime().availableProcessors(); ;



    public static class CustomThreadFactory implements ThreadFactory{

        private final String tag;
        private ThreadGroup threadGroup;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private static final AtomicInteger poolNumber = new AtomicInteger(1);


        CustomThreadFactory(String tag){
            SecurityManager s = System.getSecurityManager();
            this.threadGroup = (s != null) ? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
            this.tag = "apppool-" + poolNumber.getAndIncrement() + "-" + tag + "-";
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(threadGroup,r,
                    tag+String.valueOf(threadNumber.getAndIncrement()) );
            if (t.isDaemon()) {
                t.setDaemon(false);
            }
            if (t.getPriority() != Thread.NORM_PRIORITY) {
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        }
    }
    //混合型线程池holder 核心线程
    /**
     * 混合线程池
     */
    private static final int MIXED_CORE = 0;  //混合线程池核心线程数
    private static final int MIXED_MAX = 128;  //最大线程数
    private static final int KEEP_ALIVE_SECONDS = 30;  //空闲线程存活时间
    private static final int QUEUE_SIZE = 10000;
    private static final String MIXED_THREAD_AMOUNT = "mixed.thread.amount";
    private static class MixedTargetThreadPoolLazyHolder {
        //首先从环境变量 mixed.thread.amount 中获取预先配置的线程数
        //如果没有对 mixed.thread.amount 做配置，则使用常量 MIXED_MAX 作为线程数
        private static final int max = (null != System.getProperty(MIXED_THREAD_AMOUNT)) ?
                Integer.parseInt(System.getProperty(MIXED_THREAD_AMOUNT)) : MIXED_MAX;
        //线程池： 用于混合型任务
        private static final ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(
                max,
                max,
                KEEP_ALIVE_SECONDS,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue(QUEUE_SIZE),
                new CustomThreadFactory("mixed"));

        static {
            EXECUTOR.allowCoreThreadTimeOut(true);
            //JVM关闭时的钩子函数
            Runtime.getRuntime().addShutdownHook(new ShutdownHookThread("混合型任务线程池", new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    //优雅关闭线程池
                    shutdownThreadPoolGracefully(EXECUTOR);
                    return null;
                }
            }));
        }
    }

    public static class ShutdownHookThread extends Thread{

        Callable c;

        private volatile boolean hasShutdown = false;


        public ShutdownHookThread(String name, Callable<Void> voidCallable) {

            super("JVM退出钩子(" + name + ")");

            this.c = voidCallable;

        }

        @Override
        public void run() {
            synchronized (this) {
                log.info(getName() + " starting.... ");
                if (!this.hasShutdown) {
                    this.hasShutdown = true;
                    long beginTime = System.currentTimeMillis();
                    try {
                        this.c.call();
                    } catch (Exception e) {
                        log.info(getName() + " error: " + e.getMessage());
                    }
                    long consumingTimeTotal = System.currentTimeMillis() - beginTime;
                    log.info(getName() + "  耗时(ms): " + consumingTimeTotal);
                }
            }
        }
    }

    public static void shutdownThreadPoolGracefully(ExecutorService threadPool) {
        if (!(threadPool instanceof ExecutorService) || threadPool.isTerminated()) {
            return;
        }
        try {
            threadPool.shutdown();   //拒绝接受新任务
        } catch (SecurityException e) {
            return;
        } catch (NullPointerException e) {
            return;
        }
        try {
            // 等待 60 s，等待线程池中的任务完成执行
            if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                // 调用 shutdownNow 取消正在执行的任务
                threadPool.shutdownNow();
                // 再次等待 60 s，如果还未结束，可以再次尝试，或则直接放弃
                if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                    System.err.println("线程池任务未正常执行结束");
                }
            }
        } catch (InterruptedException ie) {
            // 捕获异常，重新调用 shutdownNow
            threadPool.shutdownNow();
        }
        //任然没有关闭，循环关闭1000次，每次等待10毫秒
        if (!threadPool.isTerminated()) {
            try {
                for (int i = 0; i < 1000; i++) {
                    if (threadPool.awaitTermination(10, TimeUnit.MILLISECONDS)) {
                        break;
                    }
                    threadPool.shutdownNow();
                }
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
            } catch (Throwable e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
