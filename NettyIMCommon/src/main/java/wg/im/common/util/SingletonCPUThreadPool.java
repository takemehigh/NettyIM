package wg.im.common.util;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

//IO密集/cpu密集混合：（（线程等待时间+cpu时间）/CPU时间）+1
//创建线程池技巧：
    //CPU密集，CPU 密集型任务也叫计算密集型任务，其特点是要进行大量计算而需要消耗 CPU 资源，比
//如计算圆周率、对视频进行高清解码等等。CPU 密集型任务虽然也可以并行完成，但是并行的任
//务越多，花在任务切换的时间就越多，CPU 执行任务的效率就越低，所以，要最高效地利用 CPU，
//CPU 密集型任务的并行执行的数量应当等于 CPU 的核心数。
    //单利懒汉线程池的创建方式(静态内部类加载，只有在调用getINstance时才会创建线程池对线）
    public class SingletonCPUThreadPool {

        static int cpuNum=Runtime.getRuntime().availableProcessors();

        static int IO_MAX=cpuNum;
        static long KEEP_ALIVE=100;
        private static class PoolHolder{
            //核心和最大一样，这是为了
            private static final ThreadPoolExecutor INSTANCE= new ThreadPoolExecutor(IO_MAX
                    ,IO_MAX,KEEP_ALIVE,TimeUnit.MILLISECONDS,new LinkedBlockingQueue<>(20),
                    Executors.defaultThreadFactory());
            static {
                //允许核心线程回收
                INSTANCE.allowCoreThreadTimeOut(true);
            }

        }


        public static final ThreadPoolExecutor getInstance(){
            return PoolHolder.INSTANCE;
        }

    public static void main(String[] args) {
                ThreadPoolExecutor threadPoolExecutor = SingletonCPUThreadPool.getInstance();
                threadPoolExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("测试IO密集");
                    }
                });
    }


}


