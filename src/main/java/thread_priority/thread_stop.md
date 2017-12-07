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

