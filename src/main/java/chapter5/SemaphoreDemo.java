package chapter5;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Created by IntelliJ IDEA.
 * User: XINGHAIFANG
 * Date: 2019/7/16
 * Time: 16:17
 * 控制同时访问的个数, 如下有20个自愿要访问这个自愿,通过acquire()和release()获取和释放访问许可.
 */
public class SemaphoreDemo {
    public static void main(String[] args) {
        //线程池方式
        ExecutorService executorService = Executors.newCachedThreadPool();
        final Semaphore semaphore = new Semaphore(5);//只能同时5个线程同时访问;

        //默认20个客户端同时访问;
        for(int index =0;index<20;index++){
            final int NO = index;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try{
                        //获得许可;
                        semaphore.acquire();
                        System.out.println("Accessing: " + NO);
                        Thread.sleep((long)(Math.random()*10000));

                        //访问完后,释放;
                        semaphore.release();
                    }catch (InterruptedException e){

                    }
                }
            };

            executorService.execute(runnable);

        }

        //客户端调用完成后,退出线程池;
        executorService.shutdown();
    }
}
