package thread_create;

/**
 * Created by fang on 2017/11/30.
 * 使用Runnable接口 + 重写run()方法
 * 1 避免单继承局限性.
 * 2 方便共享资源.
 *
 */
public class Programmer implements Runnable{

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    public void run() {
        for(int i=0;i<100;i++){
            System.out.println(" 一边敲代码....");
        }
    }
}
