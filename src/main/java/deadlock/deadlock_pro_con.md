（十五）java并发编程--线程的死锁解决方案(生产者和消费者)

上一篇中,主要了解了什么时候死锁，并且提出死锁的一个解决方案，多个锁要按照一定的顺序来。

本片主要是利用生产者消费者模式解决线程的死锁。

多线程生产者和消费者一个典型的多线程程序。一个生产者生产提供消费的东西，但是生产速度和消费速度是不同的。这就需要让生产者和消费者运行不同的线程，通过
共享区域或者队列来协调他们。如下图所示：


代码如下:
```
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

```
自从java1.5之后有很多容易的方式去实现生产者和消费者方案,最好的方式就是使用a blocking queue。

```
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

```
如果有一个生产者和一个消费者,我们可以使用count down latch,代码如下:
```
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


```

 如果一个生产者和一个消费者,我们还可以使用Exchanger,代码如下:
 ```
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

 ```

 以上几种方式就是实现生产者和消费者解决方案,同时也解决了死锁问题。
 对比以上几种方式，



