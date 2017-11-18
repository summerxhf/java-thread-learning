package unsafeThread;
//import net.jcip.annotations.*;
/**
 * Created by fang on 2017/11/17.
 */
//@NotThreadSafe
public class UnsafeSequence implements Runnable{
    private int value;
    public synchronized int getNext(){
        return value++;
    }
    public void run(){
        System.out.println(getNext());
    }

}

