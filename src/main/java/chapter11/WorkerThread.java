package chapter11;

import java.util.concurrent.BlockingQueue;

/**
 * Created by IntelliJ IDEA.
 * User: XINGHAIFANG
 * Date: 2019/7/31
 * Time: 9:26
 * 并发程序中包含串行部分: 从队列中获取任何任务,所有的工作者线程都共享同一个工作队列
 * 当一个线程从队列中获取出任务的时候,其他需要获取下一个任务的线程就必须等待, 这就是任务处理过程中的串行部分;
 */
public class WorkerThread extends Thread {
    private final BlockingQueue<Runnable> queue;

    public WorkerThread(BlockingQueue<Runnable> queue){
        this.queue = queue;
    }
    @Override
    public void run() {
        while (true){
            try{
                Runnable task = queue.take();
                task.run();
            }catch (InterruptedException e){
                break;
            }
        }
    }
}
