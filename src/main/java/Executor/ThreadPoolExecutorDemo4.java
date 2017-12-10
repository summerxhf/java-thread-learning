package Executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by fang on 2017/12/10.
 * 创建单线程化的线程池,只会用唯一的工作线程来执行,保证任务是按照(FIFO\LIFO,优先级来执行的)
 */
public class ThreadPoolExecutorDemo4 {
    public static void main(String[] args) {
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        for(int i=0;i<10;i++){
            final int index = i;
            singleThreadExecutor.execute(new Runnable() {
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
