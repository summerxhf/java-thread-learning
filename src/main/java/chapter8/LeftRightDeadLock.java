package chapter8;

/**
 * Created by IntelliJ IDEA.
 * User: XINGHAIFANG
 * Date: 2019/7/26
 * Time: 15:43
 * 简单的顺序锁死锁
 */
public class LeftRightDeadLock {
    private final Object left = new Object();
    private final Object right = new Object();

    public void leftRight(){
        synchronized (left){
            synchronized (right){
//                doSomething();
            }
        }
    }

    public void rightLeft(){
        synchronized (right){
            synchronized (left){
//                doSomethingElse();
            }
        }
    }
}
