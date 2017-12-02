package thread_create.callable;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Created by fang on 2017/12/2.
 * 使用泛型 *
 * @param <V> the result type of method {@code call}

 */
public class CallableThreadExample_generycity implements Callable<Integer>{
    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws Exception if unable to compute a result
     */
    public Integer call() throws Exception {//利用泛型,方法的返回类型为Integer

        int i = 5;//线程返回值
        System.out.println(Thread.currentThread().getName() + " " + i);
        return i;
    }

    /**
     * main方法主线程.
     * @param args
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CallableThreadExample callableThreadExample = new CallableThreadExample();
        FutureTask futureTask = new FutureTask(callableThreadExample);

        new Thread(futureTask,"Callable创建线程体").start();

        System.out.println("Callable线程体线程返回值: " + futureTask.get());
    }
}
