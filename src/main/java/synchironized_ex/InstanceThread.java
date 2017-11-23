package synchironized_ex;

/**
 * Created by fang on 2017/11/23.
 * 线程类.
 */
public class InstanceThread extends Thread {
    public void run(){
        InstanceSynchronized instanceSynchronized = new InstanceSynchronized();
        instanceSynchronized.instanceMethod();
    }
}
