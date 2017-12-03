package thread_create;

/**
 * Created by fang on 2017/12/3.
 * 模拟网络延时.
 */
public class ThreadSleepDemo2 {
    public static void main(String[] args) {
        //真实角色.
        Web12356 web12356 = new Web12356();
        Thread thread1 = new Thread(web12356,"路人甲");
        Thread thread2 = new Thread(web12356,"路人乙");
        Thread thread3 = new Thread(web12356,"路人丙");
        //启动线程.
        thread1.start();
        thread2.start();
        thread3.start();
    }
}
