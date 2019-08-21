package chapter13;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by IntelliJ IDEA.
 * User: XINGHAIFANG
 * Date: 2019/8/21
 * Time: 9:43
 * 带有时间限制的加锁,限制在指定时间内完成,否则代码则失败;
 * 带有时间限制操作中实现独占锁;
 */
public class LockWithTimeLimit {
    private Lock lock = (Lock) new ReentrantLock();
    public boolean trySendOnSharedLine(String message,
                                       long timeout, TimeUnit timeUnit)throws InterruptedException{
        long nanosToLock = timeUnit.toNanos(timeout)-estimatedNanosToSend(message);
        if(!lock.tryLock(nanosToLock,TimeUnit.NANOSECONDS)){
            return false;
        }
        try{
            return sendOnSharedLine(message);
        }finally {
            lock.unlock();
        }
    }

    private boolean sendOnSharedLine(String message){
        return true;
    }

    private long estimatedNanosToSend(String message) {
        return message.length();
    }
}
