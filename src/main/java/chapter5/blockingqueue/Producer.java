package chapter5.blockingqueue;

import java.util.concurrent.BlockingQueue;

/**
 * Created by IntelliJ IDEA.
 * User: XINGHAIFANG
 * Date: 2019/7/15
 * Time: 17:10
 */
public class Producer implements Runnable {
    private final BlockingQueue<Integer> queue;

    @Override
    public void run() {
        for (int i = 0; i < 20; i++) {
            try {
                System.out.println("[producer] Put :" + i);
                queue.put(i);
                System.out.println("[Producer] Queue remainingCapacity: " + queue.remainingCapacity());

                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public Producer(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }
}
