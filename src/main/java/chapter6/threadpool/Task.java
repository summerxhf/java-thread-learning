package chapter6.threadpool;

import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 * User: XINGHAIFANG
 * Date: 2019/7/17
 * Time: 11:34
 */
public class Task implements Runnable{
    private String name;
    public Task(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    @Override
    public void run() {
        try{
            Long duration = (long)(Math.random()*10);
            System.out.println("Executing : " + name);
            TimeUnit.SECONDS.sleep(duration);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
