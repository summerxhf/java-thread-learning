package deadlock;

/**
 * `Created by fang on 2017/12/8.
 * 死锁简单示例.伪代码
 */
public class DeadLockDemo {
    //死锁的示例.
    public void methodA(){
        synchronized (lockA){
            //.....




            synchronized (lockB){
                //...
            }
        }
    }

    public void methodB(){
        synchronized (lockB){
            //....


            synchronized (lockA){
                //......
            }
        }
    }

    //避免死锁的伪代码.

    public void methodA(){
        synchronized (lockA){
            //.....




            synchronized (lockB){
                //...
            }
        }
    }

    public void methodB(){
        synchronized (lockA){
            //....


            synchronized (lockB){
                //......
            }
        }
    }
}
