package wg.im.common.concurrent;

import wg.im.common.util.ThreadUtil;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author gw
 * @version 1.0
 * @description: TODO
 * @date 2023/4/11 11:11 上午
 */
public class FutureTaskScheduler {
    //方法二是使用自建的线程池时，专用于处理耗时操作
    static ThreadPoolExecutor mixPool = null;

    static {
        mixPool = ThreadUtil.getMixedTargetThreadPool();
    }

    private static FutureTaskScheduler inst = new FutureTaskScheduler();

    private FutureTaskScheduler() {

    }

    /**
     * 添加任务
     *
     * @param r
     */


    public static void add(Runnable r) {
        mixPool.submit(()->{ r.run(); });
    }

}
