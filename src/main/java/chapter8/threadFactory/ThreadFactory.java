package chapter8.threadFactory;

/**
 * Created by IntelliJ IDEA.
 * User: XINGHAIFANG
 * Date: 2019/7/26
 * Time: 9:43
 * ThreadFactory接口
 */
public interface ThreadFactory {
    Thread newThread(Runnable runnable);
}
