package thread_interrupt;

/**
 * Created by fang on 2017/12/7.
 * interrupt线程终止.
 */
public class ThreadInterruptDemo {
    public static void main(String[] args) throws InterruptedException {
        MyThread myThread  = new MyThread();
        myThread.start();
        System.out.println("Starting thread...");
        Thread.sleep(3000);
        myThread.interrupt();//阻塞时推出阻塞状态.
        Thread.sleep(3000);//休眠3s观察myThread阻塞状态.
        System.out.println("stop application.......");
    }
}
