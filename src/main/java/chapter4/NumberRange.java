package chapter4;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by IntelliJ IDEA.
 * setLower()和setUpper()都是"先检查,后执行"的操作,没有足够的加锁机制来保证这些操作的原子性;
 * 所以该类不是线程安全的;
 * User: XINGHAIFANG
 * Date: 2019/7/12
 * Time: 16:35
 */
public class NumberRange {
    private final AtomicInteger lower = new AtomicInteger();
    private final AtomicInteger upper = new AtomicInteger();
    public void setLower(int i){
        if(i>upper.get()){
            throw new IllegalArgumentException("can't set lower to " + i+ " >upper");
        }
        lower.set(i);
    }

    public void setUpper(int i){
        if(i<lower.get()){
            throw new IllegalArgumentException("can't set upper to " +i+ " <lower");
        }
        upper.set(i);
    }

    public boolean isInRanger(int i){
        return (i>=lower.get() && i<=upper.get());
    }
}
