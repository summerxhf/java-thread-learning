package chapter13;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by IntelliJ IDEA.
 * User: XINGHAIFANG
 * Date: 2019/8/21
 * Time: 11:00
 * 可中断的锁获取
 */
public class InterruptibleLocking {
    private Lock lock = new ReentrantLock();
    public boolean sendOnSharedLine(String message)throws InterruptedException{
        lock.lockInterruptibly();
        try{
            return cancelableSendOnSharedLine(message);
        }finally {
            lock.unlock();
        }
    }

    private boolean cancelableSendOnSharedLine(String message) throws InterruptedException{
        return true;
    }

}
