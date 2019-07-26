package chapter8.threadFactory;

/**
 * Created by IntelliJ IDEA.
 * User: XINGHAIFANG
 * Date: 2019/7/26
 * Time: 9:41
 * 自定义线程工厂
 */
public class MyThreadFactory implements ThreadFactory {
    private final String poolName;
    public MyThreadFactory(String poolName){
        this.poolName = poolName;
    }
    public Thread newThread(Runnable runnable){
        return new MyAppThread(runnable,poolName);
    }
}
