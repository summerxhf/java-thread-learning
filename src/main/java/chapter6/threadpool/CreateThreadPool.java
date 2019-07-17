package chapter6.threadpool;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by IntelliJ IDEA.
 * User: XINGHAIFANG
 * Date: 2019/7/17
 * Time: 11:29
 */
public class CreateThreadPool {
    public static void main(String[] args) {
        //创建固定大小的线程池;
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
        //创建可缓存的线程池;
        ThreadPoolExecutor executor1 = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        //创建任务类型,固定大小线程池;
        ThreadPoolExecutor executor2 = (ThreadPoolExecutor) Executors.newScheduledThreadPool(10);
        //创建单线程任务;
//        ThreadPoolExecutor executor3 = (ThreadPoolExecutor)Executors.newSingleThreadExecutor();


        for (int i = 0; i < 5; i++) {
            Task task = new Task("task " + i);
            System.out.println("Create : " + task.getName());
//            executor.execute(task);
            executor1.execute(task);
//            executor2.execute(task);
//            executor3.execute(task);
        }

//        executor.shutdown();
        executor1.shutdown();
//        executor2.shutdown();
//        executor3.shutdown();

    }
}
