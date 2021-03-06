package unsafeThread;

import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by fang on 2017/11/18.
 */
@ThreadSafe
public class SafeSequence implements Runnable{
    private final AtomicLong value = new AtomicLong(0);

    public int getNext(){
        value.incrementAndGet();

        return  (int)value.get();
    }

    public void run(){
        System.out.println(getNext());
    }

}
