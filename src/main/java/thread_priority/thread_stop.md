（十三）java并发编程--线程的停止
# 1、自己添加线程退出标志位。
如下代码所示：
```
package thread_priority;

/**
 * Created by fang on 2017/12/3.
 *
 */
public class MyThread implements Runnable {
    private boolean flag = true;

    private int num = 0;
    public void run(){
        while (flag){
            System.out.println(Thread.currentThread().getName() + "-->" + num++);
        }
    }

    public void stop(){
        this.flag = !this.flag;
    }

}

```
调用stop方法.
```
package thread_priority;

/**
 * Created by fang on 2017/12/3.
 * 线程优先级.
 * MAX_PRIORITY 10
 * NORM_PRIORITY 5
 * MIN_PRIORITY 1
 * setPriority()方法是设置优先级.getPriority()是获取优先级.
 * 不是绝对的优先级,是概率,优先级高概率高,但不代表绝对的先后顺序.
 */
public class ThreadMain2 {
    public static void main(String[] args) throws InterruptedException {
        //两个线程没有任何的优先级.
        MyThread myThread =  new MyThread();
        Thread proxy = new Thread(myThread,"挨踢1");

        MyThread myThread2 =  new MyThread();
        Thread proxy2 = new Thread(myThread2,"挨踢2");

        //设置优先级
        //会看到proxy2 执行的多,但是并不代表先后顺序,proxy也有执行在前面的.
        proxy.setPriority(Thread.MIN_PRIORITY);
        proxy2.setPriority(Thread.MAX_PRIORITY);
        proxy.start();
        proxy2.start();
        Thread.sleep(100);
        myThread.stop();
        myThread2.stop();
    }

}

```

# 2、调用Thread stop（）方法停止线程。
java8api文章中显示,该方法已经被废弃。
因为thread.stop()方法本身是不安全的。
如下图所示

# 3、使用interrupt方法中断线程。
```
package thread_interrupt;

/**
 * Created by fang on 2017/12/7.
 */
public class MyThread extends Thread{
    public void run(){
        System.out.println("我是线程体....");
    }

}

```
thread interrupt  终止线程。
```
package thread_interrupt;

/**
 * Created by fang on 2017/12/7.
 * interrupt线程终止.
 */
public class ThreadInterruptDemo {
    public static void main(String[] args) throws InterruptedException {
        MyThread myThread  = new MyThread();
        myThread.start();
        System.out.println("Starting thread...");
        Thread.sleep(3000);
        myThread.interrupt();//阻塞时推出阻塞状态.
        Thread.sleep(3000);//休眠3s观察myThread阻塞状态.
        System.out.println("stop application.......");
    }
}

```
interrupt为thread的一个方法，查看帮助文档：
interrupt

public void interrupt()

除非中断当前线程本身总是允许,这个线程的checkAccess方法被调用时,这可能会导致一个SecurityException抛出。

如果这个线程被阻塞的调用wait(),wait(long),或wait(long, int) Object类的方法,或join(),join(long),join(long, int),sleep(long),或sleep(long, int),这个类的方法,那么它的中断状态将被清除,它将接收一个InterruptedException。

如果这个线程被阻塞的I / O操作在一个InterruptibleChannel通道将被关闭,线程的中断状态将被设置,线程会收到ClosedByInterruptException。

如果这个线程被阻塞在Selector那么线程的中断状态将从选择操作,它会立即返回,可能与一个非零值,就像选择器的wakeup方法被调用。

如果没有之前的条件举行这个线程的中断状态将被设置。

中断一个线程不是活着不需要有任何影响。

异常
SecurityException——如果当前线程不能修改这个线程
