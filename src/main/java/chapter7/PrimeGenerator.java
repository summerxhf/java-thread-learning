package chapter7;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by IntelliJ IDEA.
 * User: XINGHAIFANG
 * Date: 2019/7/18
 * Time: 10:02
 * 让素数生成器1秒钟后取消;
 * 如果任务调用了一个阻塞方法, 任务可能永远不会检查取消标志, 因此永远不会结束;
 */
public class PrimeGenerator implements Runnable {
    private static ExecutorService executorService = Executors.newCachedThreadPool();
    private final List<BigInteger> primes = new ArrayList<BigInteger>();

    private volatile boolean cancelled;
    @Override
    public void run() {
        //素数生成器任务;
        BigInteger p = BigInteger.ONE;
        while (!cancelled){
            System.out.println("随机获取素数");
            p = p.nextProbablePrime();
            synchronized (this){
                primes.add(p);
            }
            System.out.println("获取素数列表---"+  primes);
        }
    }

    public void cancel(){
        cancelled = true;
    }


    public synchronized List<BigInteger> get(){
        return new ArrayList<BigInteger>(primes);
    }

    static List<BigInteger> aSecondOfPrimes() throws InterruptedException{
        PrimeGenerator generator = new PrimeGenerator();
        executorService.execute(generator);//如果执行的时候出现了阻塞, 可能永远不会调用任务取消方法;
        try{
            //让素数运行器运行1s后取消;
            SECONDS.sleep(1);

        }finally {
            generator.cancel();
            System.out.println("任务取消------");
        }

        return generator.get();
    }

    public static void main(String[] args) throws InterruptedException {
        //获取素数执行1s获得计算的素数列表;, 并且执行1后停止;
        PrimeGenerator.aSecondOfPrimes();
    }
}
