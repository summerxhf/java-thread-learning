package deadlock.comsumer_product_solution;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.SynchronousQueue;

/**
 * Created by fang on 2017/12/8.
 * using block queue .
 */
public class producerConsumerWithBlockingQueue {

    public static class Producer implements Runnable{
        private BlockingQueue<Integer>queue;
        private int next = 0;

        public Producer(BlockingQueue<Integer> queue){
            this.queue = queue;
        }
        public void run() {
            while (true){
                try {
                    queue.put(next);
                    System.out.println(next + "product");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                next++;
            }
        }
    }

    public static class Consumer implements Runnable{
        private BlockingQueue<Integer> queue;
        private Consumer(BlockingQueue<Integer> queue){
            this.queue = queue;
        }
        public void run() {
            while (true){
                synchronized (queue){
                    Integer next ;
                    try {
                        next = queue.take();
                        System.out.println(next+"consumer");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }

    public static void main(String[] args) {
        BlockingQueue<Integer> queue = new LinkedBlockingDeque<Integer>(1);

        Thread producer1 = new Thread(new Producer(queue));
        Thread producer2 = new Thread(new Producer(queue));
        Thread consumer1 = new Thread(new Consumer(queue));
        Thread consumer2 = new Thread(new Consumer(queue));
        producer1.start();
        producer2.start();
        consumer1.start();
        consumer2.start();    }
}
