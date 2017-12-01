线程创建的几种方式.

# 创建和启动一个线程

创建一个线程.
```
Thread thread = new Thread();
```

启动java线程.
```
thread.start();
```

这两个例子并没有执行线程执行体,线程将会启动后然后立即停止。
有两种方式可以指定线程要执行的线程体。
1 、创建Thread的子类并且重写run（）方法。
2、一个对象实现Runnable接口。

# 线程的子类
第一种方式执行线程执行一些代码，是创建线程的子类，并且重写Thread父类的方法run（）方法。run（）方法在我们调用start（）方法后将会被执行。
如下代码：
```
package thread_create;

/**
 * Created by fang on 2017/12/1.
 * 线程创建的第一种方式,继承Thread类,并实现run()方法.
 */
public class MyThread extends Thread{

    public void run(){
        System.out.println("MyThread running");
    }

}

```
创建和启动线程上面的线程体。
 ```
  MyThread myThread  = new MyThread();
  myThread.start();
  ```
  start() 方法调用将会立刻返回只要线程启动了。它不会等待run（）方法执行完成，run（）方法可能将被不同的cup执行。当run（）
  方法执行后会打印出“MyThread running”。

  我们也可以使用匿名子类，如下代码。
```
  Thread thread = new Thread(){
            public void run(){
                System.out.println("Thread running");
            }
        };

        thread.start();

```
# Runnable 接口实现方式

  第二种方式执行线程的代码可以创建一个类实现java.lang.Runnable。Runnable的子类对象能够被一个线程执行。
  example:
```
package thread_create;

/**
 * Created by fang on 2017/12/1.
 * 第二种方式实现Runable接口
 */
public class MyRunnable implements Runnable{

    public void run() {
       System.out.println("MyRunnable running");
    }
}

```
run() 方法将被一个线程执行,传递实例对象MyRunnable 给一个线程的构造方法,如下代码.
```
    Thread thread = new Thread(new MyRunnable());
    thread.start();
```

线程开始将会调用run()方法,MyRunnable()实例将会替代Thread自己的run()方法,会打印出"MyRunnable running".

我们可以可以使用匿名类实现接口.
```
 // 匿名类实现接口.
        Runnable myRunnable = new Runnable() {
            public void run() {
                System.out.println("Runnable running");
            }
        };
        Thread thread1 = new Thread(myRunnable);
        thread1.start();
```
# 继承还是使用Runnable?
没有规定那个最好,这两种方式都可以。个人认为，我更喜欢使用Runnable，Thread将会获得实现Runnable接口的实例。当我们使用线程池的时候，很容易将
Runnable实例排好队，直到池中空闲。但对于线程子类来说有些困难。

# 通常的陷阱:调用run()方法替代start()

当我们创建和开始一个线程的时候可能直接调用run（）方法替代调用start（）方法，如下。
Thread newThread = new Thread(myRunnable);
newTherad.run();

执行的时候我们可能发现run()方法执行的和我们期望的一样,但是它并不是我们自己启动的线程执行的,而是由执行上述两行代码的线程执行的,例如可以能是
在main()主线程中执行的。如果我们想要使用我们newThread所调用的Runnable中的run()方法,必须使用newThead.run();

# 线程的名字
当你创建一个java线程,我们可以给线程给名字. 线程的名字能够帮助我们区分线程之间的不同. 例如,如果多线程中我们可以通过System.out 输出线程的名字,
我们可以看到哪个线程在执行,例如.

```
 //匿名类方式.
        Thread thread = new Thread("New Thread"){
            public void run(){
                System.out.println("run by:" + getName());
            }
        };

        thread.start();
        System.out.println(thread.getName());
```
使用Runnable方式也可以传递线程的名字.如下代码

```
  Thread thread = new Thread(new MyRunnable(),"New Thread");
        thread.start();
        System.out.println(thread.getName());
```
MyRunnable 类不是Thread的子类,它不能访问执行它线程的getName()方法.

# Thread.currentThread()
currentThread()方法返回正在执行currnetThread()线程的实例引用。通过这种方式，可以访问java的线程对象，该对象表示执行给定代码块的线程。
如下实例
```
    String threadName = Thread.currentThread().getName();
    System.out.println("当前线程名称" +threadName);
```

# java 线程例子
```
package thread_create;

/**
 * Created by fang on 2017/12/1.
 */
public class ThreadExample {
    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName());

        for(int i=0;i<10;i++){
            new Thread(" "+i){
                public void run(){
                    System.out.println("Thread: " + getName() + " running" );
                }
            }.start();
        }
    }
}

```

**执行结果:**

"C:\Program Files (x86)\Java\jdk1.8.0_144\bin\java" -Didea.launcher.port=7540 "-Didea.launcher.bin.path=C:\Program Files (x86)\JetBrains\IntelliJ IDEA 15.0.3\bin" -Dfile.encoding=UTF-8 -classpath "C:\Program Files (x86)\Java\jdk1.8.0_144\jre\lib\charsets.jar;C:\Program Files (x86)\Java\jdk1.8.0_144\jre\lib\deploy.jar;C:\Program Files (x86)\Java\jdk1.8.0_144\jre\lib\ext\access-bridge-32.jar;C:\Program Files (x86)\Java\jdk1.8.0_144\jre\lib\ext\cldrdata.jar;C:\Program Files (x86)\Java\jdk1.8.0_144\jre\lib\ext\dnsns.jar;C:\Program Files (x86)\Java\jdk1.8.0_144\jre\lib\ext\jaccess.jar;C:\Program Files (x86)\Java\jdk1.8.0_144\jre\lib\ext\jfxrt.jar;C:\Program Files (x86)\Java\jdk1.8.0_144\jre\lib\ext\localedata.jar;C:\Program Files (x86)\Java\jdk1.8.0_144\jre\lib\ext\nashorn.jar;C:\Program Files (x86)\Java\jdk1.8.0_144\jre\lib\ext\sunec.jar;C:\Program Files (x86)\Java\jdk1.8.0_144\jre\lib\ext\sunjce_provider.jar;C:\Program Files (x86)\Java\jdk1.8.0_144\jre\lib\ext\sunmscapi.jar;C:\Program Files (x86)\Java\jdk1.8.0_144\jre\lib\ext\sunpkcs11.jar;C:\Program Files (x86)\Java\jdk1.8.0_144\jre\lib\ext\zipfs.jar;C:\Program Files (x86)\Java\jdk1.8.0_144\jre\lib\javaws.jar;C:\Program Files (x86)\Java\jdk1.8.0_144\jre\lib\jce.jar;C:\Program Files (x86)\Java\jdk1.8.0_144\jre\lib\jfr.jar;C:\Program Files (x86)\Java\jdk1.8.0_144\jre\lib\jfxswt.jar;C:\Program Files (x86)\Java\jdk1.8.0_144\jre\lib\jsse.jar;C:\Program Files (x86)\Java\jdk1.8.0_144\jre\lib\management-agent.jar;C:\Program Files (x86)\Java\jdk1.8.0_144\jre\lib\plugin.jar;C:\Program Files (x86)\Java\jdk1.8.0_144\jre\lib\resources.jar;C:\Program Files (x86)\Java\jdk1.8.0_144\jre\lib\rt.jar;D:\code\java-thread-learning\target\classes;D:\m2\repository\net\jcip\jcip-annotations\1.0\jcip-annotations-1.0.jar;C:\Program Files (x86)\JetBrains\IntelliJ IDEA 15.0.3\lib\idea_rt.jar" com.intellij.rt.execution.application.AppMain thread_create.ThreadExample
main
Thread:  0 running
Thread:  1 running
Thread:  2 running
Thread:  4 running
Thread:  3 running
Thread:  6 running
Thread:  5 running
Thread:  7 running
Thread:  9 running
Thread:  8 running

Process finished with exit code 0


即使线程按照顺序启动的(start()),但是它们可能不会按照顺序执行,这意味着线程1可能不是第一个将其名称写入System.out的线程,这是因为线程在原则上是并行执行的,
而不是按照顺序执行的。JVM和操作系统决定了线程的执行顺序，这个顺序不必是它们开始的顺序。所以可能会输出如上结果。

# 线程显示异常和线程体的返回值?Callable、Future使用

上述两种方式在线程执行体run()方法上并没有线程的返回值,以及可以对外声明异常.
Java 5引入了Java.util.concurrent。并发包中的可调用接口，它类似于Runnable接口，但它可以返回任何对象，并能够抛出异常。
这篇有些长,下一篇使用Callable 和Future完成携带返回值的线程创建.



<font size="2">翻译来自:http://tutorials.jenkov.com/java-concurrency/creating-and-starting-threads.html</font>