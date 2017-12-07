（十二）java并发编程--线程优先级
如下代码:


>线程的优先级并不能保证现成的执行次序。只不过，优先级高的线程获取CPU资源的概率较大，优先级低的也并不是没有机会执行。<br>

>优先级用1-10的整数表示，数值越大优先级越高，默认优先级为5。<br>

>在一个线程中开启另外一个线程,则新开线程称为该线程的子线程,子线程初始优先级与父线程相同。

如下代码中，


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

执行结果如下(一部分执行结果)：并不是所有的第二个线程都在线程1的输出之前,但是明显线程2打印的较多,也就是线程2
执行的概率较多。
```
挨踢2-->0
挨踢2-->1
挨踢1-->0
挨踢2-->2
挨踢1-->1
挨踢2-->3
挨踢1-->2
挨踢2-->4
挨踢2-->5
挨踢2-->6
挨踢2-->7
挨踢2-->8
挨踢2-->9
挨踢2-->10
挨踢2-->11
挨踢2-->12
挨踢2-->13
挨踢2-->14
挨踢2-->15
挨踢2-->16
```



