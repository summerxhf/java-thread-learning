package thread_priority;

/**
 * Created by fang on 2017/12/3.
 *
 */
public class MyThread implements Runnable {
    private boolean flag = true;

    private int num = 0;
    public void run(){
        while (flag){
            System.out.println(Thread.currentThread().getName() + "-->" + num++);
        }
    }

    public void stop(){
        this.flag = !this.flag;
    }

}
