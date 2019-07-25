package chapter8;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.Semaphore;

/**
 * Created by IntelliJ IDEA.
 * User: XINGHAIFANG
 * Date: 2019/7/25
 * Time: 17:33
 * 有界队列以及"调用者"饱和策略
 * 通过Semaphore信号量来限制任务到达率
 */
public class BoundedExecutor {
    private final Executor exec;

    private final Semaphore semaphore;

    public BoundedExecutor(Executor exec,int bound){
        this.exec = exec;
        this.semaphore = new Semaphore(bound);
    }

    public void submitTask(final  Runnable command) throws InterruptedException{
        semaphore.acquire();
        try{
            exec.execute(new Runnable() {
                @Override
                public void run() {
                    try{
                        command.run();
                    }finally {
                        semaphore.release();
                    }
                }
            });
        }catch (RejectedExecutionException e){
            semaphore.release();
        }
    }
}
