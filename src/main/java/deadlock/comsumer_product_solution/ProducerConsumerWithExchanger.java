package deadlock.comsumer_product_solution;

import java.util.concurrent.Exchanger;

/**
 * Created by fang on 2017/12/8.
 * 使用exchanger.
 */
public class ProducerConsumerWithExchanger {
    public static class Producer implements Runnable {
        private Exchanger<Integer> exchanger;
        private int next = 0;

        public Producer(Exchanger<Integer> exchanger) {
            this.exchanger=exchanger;
        }

        public void run() {
            while (true) {
                try {
                    exchanger.exchange(next);
                    System.out.println(next  +"producer");
                } catch (InterruptedException e) {
                }
                next++;
            }
        }
    }

    public static class Consumer implements Runnable {
        private Exchanger<Integer> exchanger;
        public Consumer(Exchanger<Integer> exchanger) {
            this.exchanger=exchanger;
        }

        public void run() {
            while (true) {
                try {
                    System.out.println(exchanger.exchange(0) + "consumer");
                } catch (InterruptedException e) {
                }
            }
        }
    }

    public static void main(String args[]) throws Exception {
        Exchanger<Integer> exchanger=new Exchanger<Integer>();
        Thread producer1 = new Thread(new Producer(exchanger));
        Thread consumer1 = new Thread(new Consumer(exchanger));
        producer1.start();
        consumer1.start();
    }
}
