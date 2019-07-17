package chapter5;

import javax.annotation.processing.SupportedSourceVersion;
import java.util.concurrent.*;

/**
 * Created by IntelliJ IDEA.
 * User: XINGHAIFANG
 * Date: 2019/7/17
 * Time: 9:13
 */
public class CyclicBarrierDemo {
    private static final ThreadPoolExecutor threadPool = new ThreadPoolExecutor
            (4,10,60,TimeUnit.SECONDS,new LinkedBlockingQueue<Runnable>());

    private static final CyclicBarrier cb = new CyclicBarrier(4, new Runnable() {
        @Override
        public void run() {
            System.out.println("四个人一起出发去球场");
        }
    });


    private static class GoThread extends Thread{
        private final String name;
        public GoThread(String name){
            this.name = name;
        }

        public void run(){
            System.out.println(name + "开始从家里出发");
            try{
                Thread.sleep(1000);
                cb.await();
                System.out.println(name + "到楼底下了");
                Thread.sleep(1000);
                System.out.println(name + "到达球场");
            }catch (InterruptedException e){
                e.printStackTrace();
            }catch (BrokenBarrierException e){
                e.printStackTrace();
            }
        }


        public static void main(String[] args) {
            String [] nameStrings = {"张三","李四","王五","xinghf"};
            for(int i=0;i<4;i++){
                threadPool.execute(new GoThread(nameStrings[i]));
            }

            try{
                Thread.sleep(4000);
                System.out.println("四个人都到达了球场, 可以开始打球了");
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

}
