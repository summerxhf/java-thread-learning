package Executor;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by fang on 2017/12/10.
 * Executor 生命周期管理办法.
 */
public interface ExecutorServiceSourceCode {
    void shutdown();
    List<Runnable> shutdownNow();
    boolean isShutdown();
    boolean isTerminated();
    boolean awaitTermination (long timeout, TimeUnit unit) throws InterruptedException;

    //.....一些其他用于任务提交的便利方法.
}
