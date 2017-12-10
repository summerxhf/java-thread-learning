package Executor;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by fang on 2017/12/10.
 * 是使用可缓存的线程池,如果需要处理的任务大于线程池的长度,可灵活回收线程,
 * 若无可回收,则建立新的线程.
 *
 * 因为线程池无限大,所以当执行第二个任务的时候第一个任务已经执行完成,会复用第一个任务的线程,而不用每次都新建线程.
 */
public class ThreadPoolExecutorDemo1 {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        for(int i = 0;i<10;i++){
            final int index = i;
            Thread.sleep(index*1000);
            cachedThreadPool.execute(new Runnable() {
                public void run() {
                    System.out.println(index);
                }
            });
        }
    }
}
