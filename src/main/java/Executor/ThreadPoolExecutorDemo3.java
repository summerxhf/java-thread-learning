package Executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by fang on 2017/12/10.
 * 创建一个定长的线程池,支持定时以及周期性任务执行。代码如下。
 *
 */
public class ThreadPoolExecutorDemo3 {
    public static void main(String[] args) throws InterruptedException {
        //延期3s执行
        ScheduledExecutorService scheduledThreadPool1  = Executors.newScheduledThreadPool(5);
        for(int i = 0;i<10;i++){
            scheduledThreadPool1.schedule(new Runnable() {
                public void run() {
                    System.out.println("delay 3 seconds");
                }
            },3,TimeUnit.SECONDS);
        }

        //表示延迟1s后每3s执行一次.
        ScheduledExecutorService scheduledThreadPool2 = Executors.newScheduledThreadPool(5);
        scheduledThreadPool2.scheduleAtFixedRate(new Runnable() {
            public void run() {
                System.out.println("delay 1 seconds, and excute every 3 seconds");
            }
        }, 1, 3, TimeUnit.SECONDS);
    }
}
