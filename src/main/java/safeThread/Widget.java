package safeThread;

/**
 * Created by fang on 2017/11/19.
 */
public class Widget {
    public synchronized void doSomething(){
        System.out.println("i'm father, in execute...");
    }
}
