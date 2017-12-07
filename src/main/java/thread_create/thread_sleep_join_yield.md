（十四）线程的阻塞
[toc]


java中我们可以使用线程类的三种方式来阻止线程的执行。
线程的状态图如下（图片来自网络）：
![这里写图片描述](http://img.blog.csdn.net/20171207231918562?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvbG92ZXN1bW1lcmZvcmV2ZXI=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

# 1、yield（）
yield英文的意思是屈服，如同其意，当前线程屈服，暂停，让同等优先级的线程执行。
yield()方法可以暂停当前执行的线程,以便为相同优先级的其他等待线程提供机会, 如果没有等待线程, 或者等待线程的优先级较低, 那么相同的线程将会执行。当获得执行机会，产生的线程，由线程的调度程序决定的。如下代码所示:

```
package thread_create;

/**
 * Created by fang on 2017/12/3.
 * 暂停自己,让出cpu调度.yield
 */
public class ThreadYieldDemo extends Thread{
    //yield 暂停线程,是一个静态方法.
    public static void main(String[] args) throws InterruptedException {
        ThreadYieldDemo threadYieldDemo = new ThreadYieldDemo();
        Thread thread = new Thread(threadYieldDemo);//xinzheng
        thread.start();//就绪状态.
        //cpu调度进入运行.


        for(int i=0;i<100;i++){
            if(i%20==0){
                //暂停本线程.
                //写在谁的线程里面就暂停谁.;
                Thread.yield();//是一个静态方法.写在main中暂停main()
            }
            System.out.println("main....." + i);
        }
    }


    public void run(){
        for(int i=0;i<100;i++){
            System.out.println("yield...." +i);
        }
    }


}

```
# 2、join（）
任何执行的程序在线程t1上调用t2.join（）方法，则t1将进入等待状态，直到t2执行完成t1则继续执行。

```
package thread_create;

/**
 * Created by fang on 2017/12/3.
 *合并线程,join
 */
public class ThreadJoinDemo extends Thread {



    public static void main(String[] args) throws InterruptedException {
        ThreadJoinDemo threadJoinDemo = new ThreadJoinDemo();
        Thread thread = new Thread(threadJoinDemo);//xinzheng
        thread.start();//就绪状态.
        //cpu调度进入运行.


        for(int i=0;i<100;i++){
            if(i==50){
                thread.join();//前50是join和main执行顺序不定,当i==50加上join,则thread先执行完,main再执行.
                //main阻塞
            }
            System.out.println("main....." + i);
        }
    }

    public void run(){
        for(int i=0;i<100;i++){
            System.out.println("join...." +i);
        }
    }
}

```
我们这里threadJoinDemo是t2, 所以会在i=50的时候会先执行threadJoinDemo, 最后在执行main主线程。
# 3、sleep（）
根据我们的要求，可以让线程在指定的时间段内处于睡眠状态。

```
package thread_create;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by fang on 2017/12/3.
 * sleep demo,暂停当前线程,并不会释放锁.
 * 倒数10个数,1s打印一个.
 */
public class ThreadSleepDemo {
    public static void main(String[] args) throws InterruptedException {
        //倒数10个数,打印.
        int num=10;
        while (true){
            System.out.println(num--);
            Thread.sleep(1000);//暂停.

            if(num<=0){
                break;
            }
        }
    }
}

```
sleep（）不会释放资源，直到到了指定时间。
# 4、wait（）

wait()方法一般和notify()、notifyAll（）方法使用，这三个方法用于协调多线程对共享数据的存取，所以必须在synchronized块内使用，
也就是说，wait（）、notify、notifyAll（）的任务在调用这些方法前必须拥有对象的锁。他们是Object的方法，并不是Thread类的方法。

**为什么wait（）、notify（）、notifyAll（）在Object的方法中，却没有在Thread中类方法中？**
因为都是锁级别的操作，锁属于对象，所以把他们定义到Object中，任何一个对象都可能获得一把锁。

对象的wait()方法就不再这里列举代码了,下篇主要说一下线程的死锁,下篇补充上代码示例。

总结：

应该有全局观吧，怎么的方式效率最高，怎样的方式让自己的大脑印象更深刻？




