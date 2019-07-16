package chapter5.future_task;


import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * Created by IntelliJ IDEA.
 * User: XINGHAIFANG
 * Date: 2019/7/16
 * Time: 11:40
 * 带有返回值futuretask
 */
public class FutureTaskTest {
    private final FutureTask<String> futureTask = new FutureTask<String>(
            new Callable<String>() {
                @Override
                public String call()   {
                    return "返回值";
                }
            }
    );

    private final Thread thread = new Thread(futureTask);

    public void start(){thread.start();}

    public String get() {
        try{
            return futureTask.get();
        }catch (Exception e){
            return null;
        }
    }


    public static void main(String[] args){
        FutureTaskTest futureTaskTest = new FutureTaskTest();
        futureTaskTest.start();
        System.out.println(futureTaskTest.get());
    }
}
