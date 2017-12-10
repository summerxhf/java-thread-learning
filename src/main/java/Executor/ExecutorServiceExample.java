package Executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by fang on 2017/12/10.
 * 停止线程.
 */
public class ExecutorServiceExample {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        for(int i =0;i<100;i++){
            executorService.submit(new NewTask());
        }

        executorService.shutdown();
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
    }
}

class NewTask implements Runnable{
    public void run() {

    }
}
