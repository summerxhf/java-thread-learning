package chapter13;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

/**
 * Created by IntelliJ IDEA.
 * User: XINGHAIFANG
 * Date: 2019/8/21
 * Time: 9:19
 *  动态顺序死锁问题:使用tryLock获取两个锁,不能同时获得则退出;
 *  休眠时间包括固定部分和随机部分;
 *  指定时间内不能获得所需要的锁, 将返回一个失败状态,使操作平缓的失败;
 */
public class DeadLockAvoidance {
    private static Random rnd = new Random();

    private static final int DELAY_FIXED =1;
    private static final int DELAY_RANDOM=2;

    public boolean transferMoney(Account fromAcct,Account toAcct,
                                 DollarAmount amount,long timeout,TimeUnit unit) throws InterruptedException, InsufficientFundsException {

        long fixedDelay = getFixedDelayComponentNanos(timeout,unit);
        long randMod = getRandomDelayModulusNanos(timeout,unit);
        long stopTime = System.nanoTime() + unit.toNanos(timeout);

        while (true){
            if(fromAcct.lock.tryLock()){
                try{
                    if(toAcct.lock.tryLock()){
                        try{
                            if(fromAcct.getBalance().compareTo(amount)<0){
                                throw new InsufficientFundsException();
                            }else {
                                fromAcct.debit(amount);
                                toAcct.credit(amount);
                                return true;
                            }
                        }finally {
                            toAcct.lock.unlock();
                        }
                    }
                }finally {
                    fromAcct.lock.unlock();
                }
             if(System.nanoTime()<stopTime){
                    return false;
             }
             NANOSECONDS.sleep(fixedDelay + rnd.nextLong() % randMod);
            }
        }
    }
    static class DollarAmount implements Comparable<DollarAmount>{
        public int compareTo(DollarAmount other){
            return 0;
        }

        DollarAmount(int dollars){

        }
    }
    class Account{
        public Lock lock;

        void debit(DollarAmount d){
        }
        void credit(DollarAmount dollarAmount){

        }
        DollarAmount getBalance(){
            return null;
        }
    }
    class InsufficientFundsException extends Exception{

    }
    static long getFixedDelayComponentNanos(long timeout, TimeUnit unit){
        return DELAY_FIXED;
    }

    static long getRandomDelayModulusNanos(long timeout,TimeUnit unit){
        return DELAY_RANDOM;
    }
}
