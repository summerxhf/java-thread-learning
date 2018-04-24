package unsafeThread;
//import net.jcip.annotations.*;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.NotThreadSafe;
import net.jcip.annotations.ThreadSafe;

/**
 * Created by fang on 2017/11/17.
 */
@ThreadSafe //标注为线程安全的类.
public class UnsafeSequence implements Runnable{
    @GuardedBy("this") private int value;//注释为被保护的.
    public synchronized int getNext(){
        return value++;
    }
    public void run(){
        System.out.println(getNext());
    }

}

