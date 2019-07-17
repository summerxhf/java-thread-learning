package chapter6;

import java.util.concurrent.Executor;

/**
 * Created by IntelliJ IDEA.
 * User: XINGHAIFANG
 * Date: 2019/7/17
 * Time: 10:08
 * 为每一个请求创建新线程的Executor
 */
public class ThreadPerTaskExecutor implements Executor{
    public void execute(Runnable r){
        new Thread(r).start();
    }
}
