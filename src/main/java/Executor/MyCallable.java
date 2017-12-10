package Executor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by fang on 2017/12/10.
 * callable future ExecutorService example
 */
public class MyCallable implements Callable<String>{

    public String call() throws Exception {
        Thread.sleep(1000);

        return Thread.currentThread().getName();
    }

    public static void main(String[] args) {
        //get ExecutorService form Executors unility class ,thread pool is10
        ExecutorService executor = Executors.newFixedThreadPool(10);
        //create a list to hold the Future object associated with Callabel;
        List<Future<String>> futurelist = new ArrayList<Future<String>>();

        //create MyCallable instance
        Callable<String> callable = new MyCallable();
        for(int i=0;i<100;i++){
            //submit Callable tasks to be executed by thread pool
            Future<String> future = executor.submit(callable);
            //add future to list ,we can get return value using future .
            futurelist.add(future);
        }

        for(Future<String> future:futurelist){
            try {
                //print the return value of future ,notice the output delay in console
                //because Future.get() waits for task to get completed.
                System.out.println(new Date() + "::" + future.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        //shut down the execuutor service now
        executor.shutdown();
    }
}
