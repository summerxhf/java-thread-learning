package java_threadlocal;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by fang on 2017/11/29.
 * threadLocal实例
 */
public class ThreadLocalEx implements Runnable{
    private ThreadLocal myThreadLocal = new ThreadLocal<String>(){
        @Override protected String initialValue(){
            return "this is initial value";
        }
    };

    private static final ThreadLocal<SimpleDateFormat> formatter = new ThreadLocal<SimpleDateFormat>(){
        @Override
        protected SimpleDateFormat initialValue(){
            return new SimpleDateFormat("yyyyMMdd HHmm");
        }
    };

    public String formatIt(Date date){
        return formatter.get().format(date);
    }

    public void run(){
        System.out.println("thread Name=" + Thread.currentThread().getName() + "default fomatter" + formatter.get().toPattern());
        try{
          Thread.sleep(new Random().nextInt(1000));
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        formatter.get().toPattern();
    }

    public static void main(String[] args) throws InterruptedException {
        ThreadLocalEx threadLocalEx = new ThreadLocalEx();
        for(int i =0;i<10;i++){
            Thread thread = new Thread(threadLocalEx,""+i);
            Thread.sleep(new Random().nextInt(1000));
            thread.start();
        }
    }
}


