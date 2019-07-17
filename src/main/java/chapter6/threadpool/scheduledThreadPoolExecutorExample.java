package chapter6.threadpool;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 * User: XINGHAIFANG
 * Date: 2019/7/17
 * Time: 11:42
 */
public class scheduledThreadPoolExecutorExample {
    public static void main(String[] args) {
        ScheduledThreadPoolExecutor executor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(2);

        MyTask task = new MyTask("repeat task");
        System.out.println("Create : " + task.getName());
        //设置任务每两秒执行一次;
        executor.scheduleWithFixedDelay(task,2,2, TimeUnit.SECONDS);
    }
}

class MyTask implements Runnable{
    @Override
    public void run() {
        System.out.println("Executing : " + name + " , Current Seconds : " + new Date().getSeconds());
    }

    private String name;
    public MyTask(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
}


