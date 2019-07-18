package chapter7;

import java.math.BigInteger;
import java.util.concurrent.BlockingQueue;

/**
 * Created by IntelliJ IDEA.
 * User: XINGHAIFANG
 * Date: 2019/7/18
 * Time: 10:33
 *  使用中断而不是boolean标志来请求取消;
 */
public class PrimeProducer extends Thread {
    private final BlockingQueue<BigInteger> queue;

    private PrimeProducer(BlockingQueue<BigInteger> queue){
        this.queue = queue;
    }

    @Override
    public void run() {
        try{
            BigInteger p = BigInteger.ONE;
            while (!Thread.currentThread().isInterrupted()){
                queue.put(p = p.nextProbablePrime());
            }
        }catch (InterruptedException e){
            //允许线程退出;
        }
    }
}
