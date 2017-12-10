(十七)java并发编程--任务执行之延迟任务和周期任务Timer的使用

(十七)java并发编程--任务执行之线程池的使用
大多数并发程序围绕着"任务执行"来构造的: 任务通常是一些抽象的且离散的工作单元。通过把一个用程序的共工作分解到多个任务中，可以简化程序的组织结构，
提供一种自然的事务边界来优化错误恢复过程，以及提供一种自然的工作结构来提升并发性。
# 1 在线程中执行任务
前面的博客中主要说的线程本身，但是我如何管理和合理使用这些线程呢？
当围绕着“任务执行”来设计应用程序结构时，第一步要找到清晰的任务边界。在理想情况下，各个任务之间是互相独立的：任务并不依赖于其他任务的状态、结果或
边界效应。
大多数服务器应用提供了一种自然的任务边界选择方式：以独立的请求为边界。WEb服务器、邮件服务器、文件服务器、EJB服务器以及数据库服务器等，这些服务器都能
通过网络接受远程客户的连接请求。将独立的请求作为边界，既可以实现任务的独立性，又可以实现合理的任务规模。

# 2 串行执行任务
线程最简单的策略就是单个线程中串行地执行各项任务。如下，SingleThreadWebServer将会串行地处理它的任务。
```
package Executor;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by fang on 2017/12/9.
 */
public class SingleThreadWebServer {
    public static void main(String[] args) throws Exception {
        ServerSocket socket = new ServerSocket(80);
        while (true){
            Socket connection = socket.accept();
            handleRequest(connection);
        }
    }

    private static void handleRequest(Socket connection) {
        //...
    }
}
```
主线程在接受连接与处理相关请求等操作之间不断的交替运行。当服务器正在处理请求时，新到来的连接必须等待直到处理完成，然后服务器
再次调用accept。
这里面，web请求处理中包含一组不同运算与io操作，服务其必须处理套接字（socket）IO以读取请求和写回响应，这些操作通常会由于网络阻塞或连通性问题
被阻塞。此外服务器还可能处理文件IO或者数据库请求，这些操作同样会阻塞。
服务器的资源利用率非常低，因为单线程在等待IO操作完成时，cpu将处于空闲状态。
## 2.1 显示的为任务创建线程
那为每一个请求创建一个新的线程来提供服务，从而实现高响应性，如下ThreadPerTaskWebServer代码所示。
```
package Executor;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by fang on 2017/12/9.
 * 创建多个线程.
 */
public class ThreadPerTaskWebServer {
    public static void main(String[] args) throws Exception {
        ServerSocket socket = new ServerSocket(80);
        while (true){
            final Socket connection = socket.accept();
            Runnable task = new Runnable() {
                public void run() {
                    handleRequest(connection);
                }


            }
            new Thread().start();

        }
    }

    private static void handleRequest(Socket connection) {
        //...
    }

}

```
对单线程的改造后,主线程将创建一个新线程来处理请求,而不是在主循环中进行。由此可得出如下结论：
1）任务处理过程从主线程中分离出来，可使主循环能够更快的等待下一个到来的连接。这使得程序在完成前面的请求之前可以接受新的请求，从而提高响应性。
2）任务可以并行处理，从而能同时服务多个请求。如果有多个处理器，或者任务由于某种原因被阻塞、例如等待IO完成、获取锁或者资源的可能性，程序的吞吐率将提高。
3）任务处理代码必须是线程安全的，当有多个任务时，会并发的调用这段代码。

“采用为每个任务开启一个线程”的方法能提升串行执行的性能。只要**请求的到达速率**不超出**服务的请求处理能力，**那么这种方法可以同时带来更快的响应性和吞吐率。
## 2.2 无限制创建线程的不足
如果如限制的创建线程还有如下问题
1）线程生命周期开销非常高。

2）资源消耗。
3）稳定性。
# 3 Executor框架
基于上述两种方式的问题（单线程和无线多线程的缺点），我们可以通过有界队列来防止高负荷的应用耗尽内存。而线程池则简化了线程的管理工作，
jdk5中新增了java.util.concurrent 包提供了一种灵活线程池实现作为Executor框架的一部分。在java类中，任务的执行的主要抽象不是Thread，而是
Executor，如下代码。
```
public interface Executor{
    void execute(Runnable command);
}
```

>Executor 简单的接口却为异步任务执行框架提供了基础,**该框架能支撑多种不同类型的任务执行策略。**它提供了一种标准方法将任务的提交过程与执行过程解耦
开来,并用Runnable来表示任务。Executor的实现还提供了生命周期的支持，以及统计信息收集、应用程序管理机制和性能监视机制。

>Executor 基于一种生产者--消费者模式,提交任务的操作相当于生产者,执行任务的线程则相当于消费者。


## 3.1 基于Executor的web服务器
基于Executor来构建web服务器是非常容易的。如下所示，定义了一个固定长度的线程池。
```
package Executor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by fang on 2017/12/9.
 * 固定长度的线程池.
 */
public class TaskExecutionWebServer {
    private static final int NTHREADS = 100;

    private static final Executor exec  = Executors.newFixedThreadPool(NTHREADS);

    public static void main(String[] args) throws IOException{
        ServerSocket serverSocket = new ServerSocket();
        while (true){
            final Socket connection = serverSocket.accept();
            Runnable task = new Runnable() {
                public void run() {
                    handleRequest(connection);
                }


            };

            exec.execute(task);
        }
    }

    private static void handleRequest(Socket connection) {
    }
}

```
通过使用Executor将请求处理任务的提交与任务的实际执行解耦开来。通常，Executor配置是一次性的，
因此部署阶段可以完成，而提交任务的代码会不断的扩展整个程序中，增加了修改的难度。

可以为每个请求启动一个新线程的Executor。
```
public class ThreadPerTaskExecutor implements Executor{
    public void execute(Runnable r){
        new Thread(r).start();
    }
}
```
可以类似单线程的行为,以同步方式执行每个任务。
```
public class WithinThreadExecutor implements Executor{
    pulic void execute(Runnable r){
        r.run();
    }
}
```
以上三种方式只是作为基于Executor的web服务器的一个可能搭建情况。

## 3.2 执行策略
将任务提交和执行解耦开来，可以为某任务指定和修改执行策略。在执行策略中包括了“what 、where、When、How”
等方面，包括：
1）在什么（what）线程中执行任务？
2）任务按照什么（what）顺序执行（FIFO、FIFO、优先级）？
3）有多少个（how many）任务能并发执行？
4）在队列中有多少个（how many）任务在等待执行？
5）如果系统由于过载而需要绝句一个任务，那么应该选择（which）哪个任务？另外，如何通（how）知
应用程序有任务被拒绝？
6）在执行一个任务之前或之后，应该进行哪些（what）动作？
>最佳策略取决于可用的计算机资源以及对服务质量的需求,通过限制并发数量可以确保程序不会因为资源耗尽而失败,或者由于在稀缺资源上发生竞争而严重
影响性能。通过任务的提交与任务的执行分离开来，有助于在部署阶段选择与硬件资源最匹配的执行策略。

 ```
当我们看到下面这种形式的代码时：
new Thread(runnable).start()
并且你希望获得一种更灵活的执行策略时,请考虑使用Executor来代替Thread。
```


## 3.3 线程池
线程池,从字面意义上来看,是管理一组同构工作线程的资源池.
线程池之工作队列:保存了所有等待执行的任务。
线程池之工作者线程：从工作队列中获取一个线程，然后返回线程池并等待下一个任务。
java类库提供了灵活的线程池以及默认配置，可以通过调用Executors中的静态工厂方法之一来创建一个线程池。
### 3.3.1 newFixedThreadPool
固定线程池的长度,每提交一个任务就创建一个线程，直到达线程池的最大长度，这时线程池的规模将不会再变化（如果某个线程由于发生了未预期的Exception而结束
那么线程池将会补充一个线程）。
### 3.3.2 newCachedThreadPool
创建一个可缓存的线程池，如果线程池的当前规**模超过了处理需求时，****那么将会回收空闲的线程，**而当需求增加时，则添加新的线程，线程的规模不存在
任何的限制。
### 3.3.3 newSingleThreadExecutor
是一个单线程的Executor，它创建单个线程来执行任务，如果这个线程异常结束，会创建另外一个线程来替代。newSingleThreadExecutor能确保依照任务在队列中顺序
来串行执行（如FIFO、FIFO、优先级）。
### 3.3.1 newScheduledThreadPool
newScheduledThreadPool创建了一个**固定长度的线程池，**而且以**延迟或者定时的方式来执行任务，**类似Timer。

“从单个线程串行执行”到“为每个任务分配一个线程”，变成基于线程池的策略，将对应用程序的稳定性产生重大的 影响：Web服务器不会再在高负载情况下执行失败。
由于服务器不会创建千万个线程来争夺有限的CPU和内存资源，因此服务器的性能将平缓的降低。
通过Executor，可以实现各种调优、管理、监视、记录日志、错误报告和其他功能，如果不使用任务的执行框架，增加这些功能是十分困难的。

## 3.4 Executor的生命周期

可以创建一个Executor，但如何关闭Executor？Executor创建线程来执行任务。但JVM只有在所有（非守护）线程全部终止后才会退出（Executor所有线程自行完毕后退出）
因此，无法正确的关闭Executor，JVM则将无法结束。
Executor扩展了ExecutorService接口，添加了一些用于声明周期的管理办法（还有一些其他用于任务提交的便利方法），如下代码。

```
/**
 * Created by fang on 2017/12/10.
 * Executor 生命周期管理办法.
 */
public interface ExecutorService {
    void shutdown();
    List<Runnable> shutdownNow();
    boolean isShutdown();
    boolean isTerminated();
    boolean awaitTermination (long timeout, TimeUnit unit) throws InterruptedException;

    //.....一些其他用于任务提交的便利方法.
}

```
shutdown():执行平缓的关闭过程，不再接受新任务，同时等待已经提交的任务执行完成（包括未开始执行的任务）。
shutdownNow（）：执行粗暴的关闭过程，尝试取消所有运行中的任务，并且不再启动队列中尚未开始的任务。

awaitTermination：等待ExecutorServer终止结束状态。通常在调用awaitTermination后会立即调用shutdown,从而产生同步的关闭ExecutorService效果。
isTerminated：轮询Executor是否已经结束。
如下代码所示。

```
package Executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by fang on 2017/12/10.
 * 停止线程.
 */
public class ExecutorServiceExample {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        for(int i =0;i<100;i++){
            executorService.submit(new NewTask());
        }

        executorService.shutdown();
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
    }
}

class NewTask implements Runnable{
    public void run() {

    }
}

```
# 4 携带结果的任务Callable和Future

之前的文章介绍过Callable和future，下面是callable future 和线程的方式来实现，如下代码。
```
package Executor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by fang on 2017/12/10.
 * callable future ExecutorService example
 */
public class MyCallable implements Callable<String>{

    public String call() throws Exception {
        Thread.sleep(1000);

        return Thread.currentThread().getName();
    }

    public static void main(String[] args) {
        //get ExecutorService form Executors unility class ,thread pool is10
        ExecutorService executor = Executors.newFixedThreadPool(10);
        //create a list to hold the Future object associated with Callabel;
        List<Future<String>> futurelist = new ArrayList<Future<String>>();

        //create MyCallable instance
        Callable<String> callable = new MyCallable();
        for(int i=0;i<100;i++){
            //submit Callable tasks to be executed by thread pool
            Future<String> future = executor.submit(callable);
            //add future to list ,we can get return value using future .
            futurelist.add(future);
        }

        for(Future<String> future:futurelist){
            try {
                //print the return value of future ,notice the output delay in console
                //because Future.get() waits for task to get completed.
                System.out.println(new Date() + "::" + future.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        //shut down the execuutor service now
        executor.shutdown();
    }
}

```
输出结果如下:
Sun Dec 10 16:48:25 CST 2017::pool-1-thread-1
Sun Dec 10 16:48:26 CST 2017::pool-1-thread-2
Sun Dec 10 16:48:26 CST 2017::pool-1-thread-3
Sun Dec 10 16:48:26 CST 2017::pool-1-thread-4
Sun Dec 10 16:48:26 CST 2017::pool-1-thread-5
Sun Dec 10 16:48:26 CST 2017::pool-1-thread-6
Sun Dec 10 16:48:26 CST 2017::pool-1-thread-7
Sun Dec 10 16:48:26 CST 2017::pool-1-thread-8
Sun Dec 10 16:48:26 CST 2017::pool-1-thread-9
Sun Dec 10 16:48:26 CST 2017::pool-1-thread-10
Sun Dec 10 16:48:26 CST 2017::pool-1-thread-3
Sun Dec 10 16:48:27 CST 2017::pool-1-thread-2
Sun Dec 10 16:48:27 CST 2017::pool-1-thread-1
Sun Dec 10 16:48:27 CST 2017::pool-1-thread-5
Sun Dec 10 16:48:27 CST 2017::pool-1-thread-4
Sun Dec 10 16:48:27 CST 2017::pool-1-thread-8
Sun Dec 10 16:48:27 CST 2017::pool-1-thread-7
Sun Dec 10 16:48:27 CST 2017::pool-1-thread-9
Sun Dec 10 16:48:27 CST 2017::pool-1-thread-6
Sun Dec 10 16:48:27 CST 2017::pool-1-thread-10
Sun Dec 10 16:48:27 CST 2017::pool-1-thread-2
Sun Dec 10 16:48:28 CST 2017::pool-1-thread-3
Sun Dec 10 16:48:28 CST 2017::pool-1-thread-1
Sun Dec 10 16:48:28 CST 2017::pool-1-thread-5
Sun Dec 10 16:48:28 CST 2017::pool-1-thread-4
Sun Dec 10 16:48:28 CST 2017::pool-1-thread-8
Sun Dec 10 16:48:28 CST 2017::pool-1-thread-7
Sun Dec 10 16:48:28 CST 2017::pool-1-thread-9
Sun Dec 10 16:48:28 CST 2017::pool-1-thread-6
Sun Dec 10 16:48:28 CST 2017::pool-1-thread-10
Sun Dec 10 16:48:28 CST 2017::pool-1-thread-2
Sun Dec 10 16:48:29 CST 2017::pool-1-thread-3
Sun Dec 10 16:48:29 CST 2017::pool-1-thread-1
Sun Dec 10 16:48:29 CST 2017::pool-1-thread-4
Sun Dec 10 16:48:29 CST 2017::pool-1-thread-5
Sun Dec 10 16:48:29 CST 2017::pool-1-thread-7
Sun Dec 10 16:48:29 CST 2017::pool-1-thread-8
Sun Dec 10 16:48:29 CST 2017::pool-1-thread-9
Sun Dec 10 16:48:29 CST 2017::pool-1-thread-6
Sun Dec 10 16:48:29 CST 2017::pool-1-thread-10
Sun Dec 10 16:48:29 CST 2017::pool-1-thread-2
Sun Dec 10 16:48:30 CST 2017::pool-1-thread-3
Sun Dec 10 16:48:30 CST 2017::pool-1-thread-1
Sun Dec 10 16:48:30 CST 2017::pool-1-thread-5
Sun Dec 10 16:48:30 CST 2017::pool-1-thread-4
Sun Dec 10 16:48:30 CST 2017::pool-1-thread-7
Sun Dec 10 16:48:30 CST 2017::pool-1-thread-8
Sun Dec 10 16:48:30 CST 2017::pool-1-thread-9
Sun Dec 10 16:48:30 CST 2017::pool-1-thread-6
Sun Dec 10 16:48:30 CST 2017::pool-1-thread-10
Sun Dec 10 16:48:30 CST 2017::pool-1-thread-2
Sun Dec 10 16:48:31 CST 2017::pool-1-thread-3
Sun Dec 10 16:48:31 CST 2017::pool-1-thread-1
Sun Dec 10 16:48:31 CST 2017::pool-1-thread-4
Sun Dec 10 16:48:31 CST 2017::pool-1-thread-5
Sun Dec 10 16:48:31 CST 2017::pool-1-thread-7
Sun Dec 10 16:48:31 CST 2017::pool-1-thread-8
Sun Dec 10 16:48:31 CST 2017::pool-1-thread-9
Sun Dec 10 16:48:31 CST 2017::pool-1-thread-6
Sun Dec 10 16:48:31 CST 2017::pool-1-thread-10
Sun Dec 10 16:48:31 CST 2017::pool-1-thread-2
Sun Dec 10 16:48:32 CST 2017::pool-1-thread-3
Sun Dec 10 16:48:32 CST 2017::pool-1-thread-1
Sun Dec 10 16:48:32 CST 2017::pool-1-thread-5
Sun Dec 10 16:48:32 CST 2017::pool-1-thread-4
Sun Dec 10 16:48:32 CST 2017::pool-1-thread-8
Sun Dec 10 16:48:32 CST 2017::pool-1-thread-7
Sun Dec 10 16:48:32 CST 2017::pool-1-thread-9
Sun Dec 10 16:48:32 CST 2017::pool-1-thread-6
Sun Dec 10 16:48:32 CST 2017::pool-1-thread-10
Sun Dec 10 16:48:32 CST 2017::pool-1-thread-2
Sun Dec 10 16:48:33 CST 2017::pool-1-thread-3
Sun Dec 10 16:48:33 CST 2017::pool-1-thread-1
Sun Dec 10 16:48:33 CST 2017::pool-1-thread-5
Sun Dec 10 16:48:33 CST 2017::pool-1-thread-4
Sun Dec 10 16:48:33 CST 2017::pool-1-thread-7
Sun Dec 10 16:48:33 CST 2017::pool-1-thread-8
Sun Dec 10 16:48:33 CST 2017::pool-1-thread-9
Sun Dec 10 16:48:33 CST 2017::pool-1-thread-6
Sun Dec 10 16:48:33 CST 2017::pool-1-thread-10
Sun Dec 10 16:48:33 CST 2017::pool-1-thread-2
Sun Dec 10 16:48:34 CST 2017::pool-1-thread-3
Sun Dec 10 16:48:34 CST 2017::pool-1-thread-1
Sun Dec 10 16:48:34 CST 2017::pool-1-thread-5
Sun Dec 10 16:48:34 CST 2017::pool-1-thread-4
Sun Dec 10 16:48:34 CST 2017::pool-1-thread-7
Sun Dec 10 16:48:34 CST 2017::pool-1-thread-8
Sun Dec 10 16:48:34 CST 2017::pool-1-thread-9
Sun Dec 10 16:48:34 CST 2017::pool-1-thread-6
Sun Dec 10 16:48:34 CST 2017::pool-1-thread-10
Sun Dec 10 16:48:34 CST 2017::pool-1-thread-2
Sun Dec 10 16:48:35 CST 2017::pool-1-thread-3
Sun Dec 10 16:48:35 CST 2017::pool-1-thread-1
Sun Dec 10 16:48:35 CST 2017::pool-1-thread-5
Sun Dec 10 16:48:35 CST 2017::pool-1-thread-4
Sun Dec 10 16:48:35 CST 2017::pool-1-thread-8
Sun Dec 10 16:48:35 CST 2017::pool-1-thread-7
Sun Dec 10 16:48:35 CST 2017::pool-1-thread-9
Sun Dec 10 16:48:35 CST 2017::pool-1-thread-6
Sun Dec 10 16:48:35 CST 2017::pool-1-thread-10
从输出结果可以看到,线程池中建立了10个线程,线程池中的线程执行完毕就会释放,然后来了新的线程再继续创建.



小思:上篇中还在思考怎样效率最高,和录入自己的单脑中,今天有了一个办法,就是和其他人讨论问题,
或者讲给其他人,你能给别人讲明白了,就是自己真的懂了。和别人在讨论过程中，就会更加深刻，第二点是把知识转化为图，大脑对图的印象要深刻与文字。

上篇大部分来自于《java并发编程》一是感觉这本书真的挺好的，二是，貌似可能“吃的不够多”，还不能自己消化了，可以吸收一些，
需要反复的反刍。