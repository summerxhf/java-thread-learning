package chapter5;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by IntelliJ IDEA.
 * User: XINGHAIFANG
 * Date: 2019/7/16
 * Time: 17:12
 */
public class CyclicBarrierTest {
    public static void main(String[] args) {
        ExecutorService service = Executors.newCachedThreadPool();
        //等待3个线程到达某个位置, 提供await()方法;
        final CyclicBarrier cyclicBarrier = new CyclicBarrier(3);

        for(int i=0;i<5;i++){
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try{
                        Thread.sleep((long)(Math.random()*10000));
                        System.out.println("线程"+ Thread.currentThread().getName()
                                + "即将到达集合点,已有"+cyclicBarrier.getNumberWaiting() + "个已经到达,正在等候");
                        cyclicBarrier.await();

//                        Thread.sleep((long)(Math.random()*10000));
//                        System.out.println("线程" + Thread.currentThread().getName()
//                                + "即将到达集合点,已有"+cyclicBarrier.getNumberWaiting() + "个已经到达,正在等候");
//                        cyclicBarrier.await();
//
//                        Thread.sleep((long)(Math.random()*10000));
//                        System.out.println("线程" + Thread.currentThread().getName()
//                                + "即将到达集合点,已有"+cyclicBarrier.getNumberWaiting() + "个已经到达,正在等候");
//                        cyclicBarrier.await();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            };

            service.execute(runnable);
        }

        service.shutdown();
    }
}
