package thread_create.callable;

import java.util.concurrent.*;

/**
 * Created by fang on 2017/12/2.
 * 使用ExecutorService
 */
public class CallableThreadExample_ExecutorService implements Callable<Integer> {

    public Integer call() throws Exception {
        int i = 5;
        System.out.println("线程名称:" +Thread.currentThread().getName() + "-- " + i);
        return i;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        ExecutorService executor = Executors.newFixedThreadPool(2);
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);

        CallableThreadExample_ExecutorService cte = new CallableThreadExample_ExecutorService();
        Future<Integer> future = executor.submit(cte);
        System.out.println("Callable线程体线程返回值: " + future.get());
        executor.shutdown();
    }


}
