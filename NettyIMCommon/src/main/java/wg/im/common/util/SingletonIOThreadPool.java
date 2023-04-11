package wg.im.common.util;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


//创建线程池技巧：
    //IO密集，导致cpu利用率比较低，所以可以吧线程数设置成核心数*2
    //mmap+write
    // 用户io操作不直接与底层硬件产生读写，读操作时，将内核缓冲区与
    // 用户进程缓冲区做映，用户进程发起mmap调用，用户态进入内核态，
    // 从硬盘拷贝数据到读缓冲区，mmap返回，内核转用户，写操作，
    // cpu直接将读缓冲区数据在内核空间写入到socket缓冲区，从scoket
    // 缓冲区拷贝到网卡（比起read+write，减少了一个一次从用户缓冲区拷贝数据到内核socket缓冲区的操作）
    //单利懒汉线程池的创建方式(静态内部类加载，只有在调用getINstance时才会创建线程池对线）
    public class SingletonIOThreadPool{

        static int cpuNum=Runtime.getRuntime().availableProcessors();

        static int IO_MAX=cpuNum*2;
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
                ThreadPoolExecutor threadPoolExecutor = SingletonIOThreadPool.getInstance();
                threadPoolExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("测试IO密集");
                    }
                });
    }


}


