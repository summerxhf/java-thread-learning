package deadlock.comsumer_product_solution;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fang on 2017/12/8.
 * 生产者消费者经典模式.
 */
public class ClassicProducerConsumer {
    public static class Producer implements Runnable{
        private List<Integer> queue;

        private int next = 0;

        public Producer (List<Integer> queue){
            this.queue = queue;//共享资源添加数据.
        }
        public void run() {
            while (true){
                //生产资源.
                synchronized (queue){//锁住list中的对象.

                    queue.add(next);//生产时锁住这个数组
//                    System.out.println("product number" + next + "thread name: " + Thread.currentThread().getName());
                    queue.notifyAll();//唤醒其他线程.
                }

                next++;
            }
        }
    }

    public static class Consumer implements Runnable{
        private List<Integer> queue;
        public Consumer(List<Integer> queue){
            this.queue = queue;
        }
        public void run() {
            while (true){
                if(queue.size()>0){
                    Integer number = queue.remove(queue.size() -1);
                    System.out.println("cunsoumer number..."+number + "thread name: " + Thread.currentThread().getName());
                }else {
                    try {
                        queue.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void main(String args[]) throws Exception{
        List<Integer> queue = new ArrayList<Integer>();
        Thread producter1 = new Thread(new Producer(queue),"product1 thread");
        Thread producter2 = new Thread(new Producer(queue),"product2 therad");
        Thread consumer1 = new Thread(new Consumer(queue),"consumer1 therad");
        Thread consumer2 = new Thread(new Consumer(queue),"consumer2 therad");

        producter1.start();
        producter2.start();
        consumer1.start();
        consumer2.start();
    }
}
