package thread_create;

/**
 * Created by fang on 2017/12/1.
 * 线程创建的第一种方式,继承Thread类,并实现run()方法.
 */
public class MyThread extends Thread{

    public void run(){
        System.out.println("MyThread running");
    }

    public static void main(String[] args) {
        MyThread myThread  = new MyThread();
        myThread.start();
        //匿名类方式.
        Thread thread = new Thread("New Thread"){
            public void run(){
                System.out.println("run by:" + getName());
            }
        };

        thread.start();
        String threadName = Thread.currentThread().getName();
        System.out.println("当前线程名称" +threadName);
    }


}
