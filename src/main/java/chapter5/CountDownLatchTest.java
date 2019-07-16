package chapter5;

import java.util.concurrent.CountDownLatch;

/**
 * Created by IntelliJ IDEA.
 * User: XINGHAIFANG
 * Date: 2019/7/16
 * Time: 10:49
 * 线程创建后没有立即启动,为了测试n个
 * 线程并发执行某个任务需要的时间;
 * 启动门将使主线程能够同时释放所有工作线程; 而结束门使主线程能够等待最后一个线程执行完成, 而不是顺序的等待每个线程执行完成;
 */
public class CountDownLatchTest {
    public long timeTasks(int nThreads,final Runnable task) throws InterruptedException{
        final CountDownLatch startGate = new CountDownLatch(1);
        final CountDownLatch endGate = new CountDownLatch(nThreads);

        for (int i =0;i<nThreads;i++){
            Thread t = new Thread(){
                public void run(){
                    try{
                      //等待五个线程一起执行, 不是一个一个的执行.
                      startGate.await();//等待,当所有的计数器减到0时,所有线程并行执行;
                      try{
                          task.run();
                      }finally {
                          endGate.countDown();//计数器减1
                      }
                    }
                    catch (InterruptedException ignored){

                    }
                }
            };
            t.start();
        }

        long start = System.nanoTime();
        startGate.countDown();
        endGate.await();
        long end = System.nanoTime();
        return end - start; //返回的是纳秒
    }

    public static void main(String[] args) throws InterruptedException {
        CountDownLatchTest countDownLatchTest = new CountDownLatchTest();
        countDownLatchTest.timeTasks(5,new myTask());
    }

    static class myTask implements Runnable{
        @Override
        public void run() {
            System.out.println(Thread.currentThread() +  "任务执行");
        }
    }
}
