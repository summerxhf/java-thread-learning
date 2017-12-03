package thread_priority;

/**
 * Created by fang on 2017/12/3.
 *  Thread.currentThread() :当前线程.
 *  setName():设置名称.
 *  getName():获取名称.
 *  isAlive():线程是否活着.
 */
public class ThreadMain {
    public static void main(String[] args) throws InterruptedException {
        MyThread myThread = new MyThread();
        Thread proxy = new Thread(myThread);//线程不起名字,就是自动编号的名称.
//        proxy.setName("test");
        System.out.println(proxy.getName());
        System.out.println(Thread.currentThread().getName());

        proxy.start();
        System.out.println("启动后的状态: " + proxy.isAlive());
        Thread.sleep(200);
        myThread.stop();//自己定义的stop.
        Thread.sleep(200);//不等待200ms则可能还未停止完毕,返回为ture.当等待200s后,则会停止完全返回false.
        System.out.println("停止后的状态: " + proxy.isAlive());
    }
}
