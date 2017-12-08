package deadlock.comsumer_product_solution;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by fang on 2017/12/8.
 * countdownlatch
 */
public class ProducerConsumerWithCountDownLatch {
    public static class Producer implements Runnable {
        private List<Integer> queue;
        private CountDownLatch latch;
        private int next = 0;

        public Producer(List<Integer> queue,CountDownLatch latch) {
            this.queue = queue;
            this.latch=latch;
        }

        public void run() {
            while (true) {
                synchronized (queue) {
                    queue.add(next);
                    System.out.println(next + "producer");
                    latch.countDown();
                }
                next++;
            }
        }
    }

    public static class Consumer implements Runnable {
        private List<Integer> queue;
        private CountDownLatch latch;

        public Consumer(List<Integer> queue,CountDownLatch latch) {
            this.queue = queue;
            this.latch=latch;
        }

        public void run() {
            while (true) {
                Integer number=null;
                synchronized (queue) {
                    if (queue.size() > 0) {
                        number = queue.remove(queue.size()-1);
                        System.out.println(number+"consumer");
                    }
                }
                if(number==null) {
                    try {
                        latch.await();
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
    }

    public static void main(String args[]) throws Exception {
        List<Integer> queue = new ArrayList<Integer>();
        CountDownLatch latch=new CountDownLatch(1);
        Thread producer1 = new Thread(new Producer(queue,latch));
        Thread consumer1 = new Thread(new Consumer(queue,latch));
        producer1.start();
        consumer1.start();
    }
}

