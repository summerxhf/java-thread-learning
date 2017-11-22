package safeThread;

/**
 * Created by fang on 2017/11/19.
 * Synchronized 可重复锁,举例.
 */
public class ReentrantSynchronized {
    public volatile int  a = 1;
    /**
     * main主线程.
     * @param args
     */
    public static void main(String[] args) {
        LoggingWidget loggingWidget = new LoggingWidget();
        loggingWidget.doSomething();

    }
}
