package safeThread;

/**
 * Created by fang on 2017/11/19.
 */
public class LoggingWidget extends Widget {
    public synchronized void doSomething(){
        System.out.println("child " +  toString() + ": calling doSomething");
        super.doSomething();
    }
}
