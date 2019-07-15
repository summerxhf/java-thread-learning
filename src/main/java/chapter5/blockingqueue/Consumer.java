package chapter5.blockingqueue;

import jdk.nashorn.internal.ir.Block;

import java.util.concurrent.BlockingQueue;

/**
 * Created by IntelliJ IDEA.
 * User: XINGHAIFANG
 * Date: 2019/7/15
 * Time: 17:17
 */
public class Consumer implements Runnable {
    private final BlockingQueue<Integer> queue;

    public void run() {
        try {
            while (true) {
                Integer take = queue.take();
                process(take);
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public Consumer(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }


    private void process(Integer take) throws InterruptedException {
        System.out.println("[Consumer] Take: " + take);
        Thread.sleep(500);
    }
}
