package Executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by fang on 2017/12/10.
 * 创建一个定长的线程池,可控制并发的最大数,超出线程会在队列中等待,代码如下.
 * 定长线程池的大小最好根据系统资源设置,如Runtime.getRuntime().availableProcessors()cpu处理器数.
 */
public class ThreadPoolExecutorDemo2 {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
        for(int i = 0;i<10;i++){
            final int index = i;
            fixedThreadPool.execute(new Runnable() {
                public void run() {
                    System.out.println(index);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
