
本篇文章用到了java并发包中的这几个接口和类
java.util.concurrent.Callable
java.util.concurrent.FutureTask
java.util.concurrent.Future
java.util.concurrent.Executor


# <font size="4">**1.使用Callable + FutureTask方式,代码如下所示。**
```
package thread_create.callable;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Created by fang on 2017/12/2.
 * callable具体示例.
 */
public class CallableThreadExample implements Callable {


    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws Exception if unable to compute a result
     */
    public Object call() throws Exception {

       int i = 5;//线程返回值
       System.out.println(Thread.currentThread().getName() + " " + i);
        return i;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CallableThreadExample callableThreadExample = new CallableThreadExample();
        FutureTask futureTask = new FutureTask(callableThreadExample);

        new Thread(futureTask,"Callable创建线程体").start();

        System.out.println("Callable线程体线程返回值: " + futureTask.get());
    }
}

```
实现过程

（1）创建类实现Callable接口，并实现接口中的call（）方法，该call（）方法作为线程的执行体，并且有返回值。
（2）创建main（）主线程，并在main（）方法中new Callable实现类的实例，使用FutureTask类包装Callable对象，FutureTask对象封装了该Callable对象的call（）方法的返回值。
（3）使用创建的FutureTask对象实例作为Thread对象的参数，并启动线程。
（4）调用FutureTask对象的get（）方法来获得子线程执行结束后的返回值。

# <font size="4">**2、在上面的基础Callable + FutureTask上加上泛型Callable<>**
```
package thread_create.callable;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Created by fang on 2017/12/2.
 * 使用泛型 *
 * @param <V> the result type of method {@code call}

 */
public class CallableThreadExample_generycity implements Callable<Integer>{
    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws Exception if unable to compute a result
     */
    public Integer call() throws Exception {//利用泛型,方法的返回类型为Integer

        int i = 5;//线程返回值
        System.out.println(Thread.currentThread().getName() + " " + i);
        return i;
    }

    /**
     * main方法主线程.
     * @param args
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CallableThreadExample callableThreadExample = new CallableThreadExample();
        FutureTask futureTask = new FutureTask(callableThreadExample);

        new Thread(futureTask,"Callable创建线程体").start();

        System.out.println("Callable线程体线程返回值: " + futureTask.get());
    }
}

```
# <font size="4">**3、Callable + Future + Executor**
代码如下。
```
package thread_create.callable;

import java.util.concurrent.*;

/**
 * Created by fang on 2017/12/2.
 * 使用ExecutorService
 */
public class CallableThreadExample_Executor implements Callable<Integer> {

    public Integer call() throws Exception {
        int i = 5;
        System.out.println("线程名称:" +Thread.currentThread().getName() + "-- " + i);
        return i;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //以下两种方式都可以.
//        ExecutorService executor = Executors.newFixedThreadPool(2);
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);

        CallableThreadExample_Executor cte = new CallableThreadExample_Executor();
        Future<Integer> future = executor.submit(cte);
        System.out.println("Callable线程体线程返回值: " + future.get());
        executor.shutdown();
    }


}

```
如下图是使用Callable+Executors的实现过程。
![这里写图片描述](http://img.blog.csdn.net/20150904191711567?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQv/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/Center)

这里用的是Callable任务，线程池也可以提交Runnable的线程体。
线程池的结构图如下：
![这里写图片描述](http://img.blog.csdn.net/20171202224435682?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvbG92ZXN1bW1lcmZvcmV2ZXI=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

在这里就先不深究了，具体源码是怎么实现的后续博客再解释。
# <font size="4">**4.与Runnable比较**
使用实现Callable接口,实现call()方法，与实现Runnable接口和继承Thread方式比较如下:
(1)上篇中所说的两种实现线程的方式都是实现方法run(),run()为线程执行体,但是run()方法并不能有携带返回值的线程体。并且不能抛出异常，只能用try catch, 因为Runnable接口中并没有对外声明异常。
（2）也没有返回值，run（）执行体方法是void类型，没有线程体的返回值。
（3）使用Callable接口,可以解决上述两种问题,不但可以携带返回值也可以声明式异常。
（4）Future表示异步计算的结果，它提供了检查计算是否完成方法，以等待计算的完成，并检索计算的结果。
通过Future对象可以进一步了解任务的执行情况，可取消的任务执行，还可以获取执行任务的结果。
# <font size="4">
**5.总结**
想到了盲人摸象，不但是在学习过程中是这样的，人生也是如此。