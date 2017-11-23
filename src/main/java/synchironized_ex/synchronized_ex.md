虽然前面文章的一些实例中已经使用synchronized关键字来实现线程的同步，但还是需要好好的理解一下。
>一段代码或者一个方法被synchronized关键字标记我们就说这断代码是同步块。同步块可以用来避免竞争条件。
><font color="red">**synchronized则是有“一个变量在同一时刻只允许一条线程对其进行lock操作”，这条规则决定了持有同一个锁的两个同步块只能串行地进入。**</font>

[TOC]

# **<font size=4>java synchronized 关键字 </font>**

java中的同步块在某些对象上是同步的。在一个对象上的所有同步块，在同一时间只能有一个线程在执行。其他的线程尝试进入同步块将会被阻塞
，直到同步块中的线程退出该代码块。
synchronized可以作用于不同类型的块。

 1. 实例方法上
 2. 静态方法上
 3. 实例方法上的代码块。
 4. 静态方法上的代码块。


----------


# <font size=4>**synchronized实例方法**
实例方法中的同步块，代码如下：

```
  public synchronized void add(int value){
      this.count += value;
  }
```


synchronized作用于一个方法上，我们说这个方法是同步方法。一个同步实例方法在java中是同步与对象实例的方法上。因此每个实例有它自己的同步方法同步在不同的对象上：他们自己的实例对象。
只有一个线程可以在同步实例方法上执行。
如果超过一个实例存在（这个类的对象有多个），在每个实例的同步方法中只能有一个线程，也就是一个线程一个实例。如下代码:
```
package synchironized_ex;

/**
 * Created by fang on 2017/11/23.
 * 类的每个实例同步示例.
 */
public class InstanceSynchronized {
    public synchronized void instanceMethod(){
        System.out.println("执行实例同步方法开始......");
         try{
            Thread.sleep(1000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        System.out.println("执行实例同步方法结束......");

    }

}
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
package synchironized_ex;

/**
 * Created by fang on 2017/11/23.
 * main方法
 */
public class ThreadClient {
    public static void main(String[] args) {
        for(int i = 0;i<3; i++){
            Thread myThread = new InstanceThread();
            myThread.start();
        }
    }
}


```
![执行结果如下](http://img.blog.csdn.net/20171123231305419?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvbG92ZXN1bW1lcmZvcmV2ZXI=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

从上面结果可以看出来，synchronized作用于类的多个实例上，每个实例保证一次只有一个线程，并不是多个实例保证一次只有一个线程。实际上是“对象锁”。

----------

# <font size=4>**synchronized静态方法**
类中的静态方法被关键字synchronized标记，如下

```
  public static synchronized void add(int value){
      count += value;
  }
```
同样，这里synchronized关键字告诉java这个方法是同步的。
在java虚拟机中一个类只能对应一个类对象（注意不是实例对象），所以同时只允许一个线程执行类对象的静态方法。
我们把上面的代码中线程执行的方法中改为静态方法：

```
package synchironized_ex;

/**
 * Created by fang on 2017/11/23.
 * 类的每个实例同步示例.
 */
public class InstanceSynchronized {
    public static synchronized void instanceMethod(){//加static和不加static的区别,锁类和锁对象.
        System.out.println("执行实例同步方法开始......");
         try{
            Thread.sleep(1000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        System.out.println("执行实例同步方法结束......");

    }

}
```
结果如下图所示。
![静态方法synchronized](http://img.blog.csdn.net/20171124005049853?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvbG92ZXN1bW1lcmZvcmV2ZXI=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

----------

看了stackoverflow一个小实例比较深刻。

To understand this, the easiest way is to compare how lock works against instance method and static method. Let's say you have class Test.java, which has two methods as follow.

```
public class Test{
   public synchronized void instanceMethod(){
   }

   public synchronized static void staticMethod(){
   }
}
```

Meanwhile, there are two instances of class Test, testA and testB. And also there are two thread tA and tB trying to access class Test in parallel.

locking on instanceMethod: When tA gets the lock on instanceMethod of testA, tB cannot access the the same method in testA, however tB is still free to invoke instanceMethod in testB. Because the synchronization against instanceMethod is instance level locking

locking on staticMethod: However, when tA gets the lock on staticMethod, the lock has nothing to do with testA or testB, since synchronization on static method is a class level locking. Which means tB cannot access staticMethod at all until tA release the lock

原理
非静态方法时对象持有锁，而对于静态方法来说是类持有锁。jdk同步方法的定义：https://docs.oracle.com/javase/specs/jls/se7/html/jls-8.html#jls-8.4.3.6
定义如下：
>A synchronized method acquires a monitor (§17.1) before it executes. For a class (static) method, the monitor associated with the Class object for the method's class is used. For an instance method, the monitor associated with this (the object for which the method was invoked) is used.

----------

# <font size=4>**实例方法中的同步块**
有时候我们不需要同步整个方法,可能需要同步的仅仅是方法中的一块代码,java可以在方法中写同步代码块,如下所示:

```
 public void add(int value){

    synchronized(this){
       this.count += value;
    }
  }
```
上述中使用了"this",调用的是add方法本身的实例. 在同步构造其中用括号括起来的对象叫做"监视器对象". 上述代码使用监视器对象同步, 同步方法使用调用方法本身的实例作为监视器对象.
一次只能有一个线程能够同步于同一个监视器对象的方法内执行.
下面的例子都同步在他们所调用的对象的实例上, 因此调用效果是等效的.

```
 public class MyClass {

  public synchronized void log1(String msg1, String msg2){
     log.writeln(msg1);
     log.writeln(msg2);
  }
   public void log2(String msg1, String msg2){
     synchronized(this){
            log.writeln(msg1);
         log.writeln(msg2);
      }
   }
 }
```
在上述例子中, 每次只有一个线程能够在同步块中的任意一个方法内执行.
如果第二个方法块不是同步在this实例对象上, 那么两个方法可以被线程同时执行.

 ----------


# <font size=4>**静态方法中的同步块**
下面是两个静态方法的同步块

```
 public class MyClass {
    public static synchronized void log1(String msg1, String msg2){
      log.writeln(msg1);
      log.writeln(msg2);
    }

   public static void log2(String msg1, String msg2){
       synchronized(MyClass.class){
          log.writeln(msg1);
          log.writeln(msg2);
       }
    }
  }
```
这两个方法不允许同时被线程访问, 如果第二个同步块不是同步在MyClass.class这个类上, 那么可以同时被线程访问.

----------

# <font size=4>**使用同步方法注意项**
	1、线程同步的目的是为了保护多个线程反问一个资源时对资源的破坏。
	2、线程同步方法是通过锁来实现，每个对象都有切仅有一个锁，这个锁与一个特定的对象关联，线程一旦获取了对象锁，其他访问该对象的线程就无法再访问该对象的其他同步方法。
	3、对于静态同步方法，锁是针对这个类的，锁对象是该类的Class对象。静态和非静态方法的锁互不干预。一个线程获得锁，当在一个同步方法中访问另外对象上的同步方法时，会获取这两个对象锁。
	4、对于同步，要时刻清醒在哪个对象上同步，这是关键。
	5、编写线程安全的类，需要时刻注意对多个线程竞争访问资源的逻辑和安全做出正确的判断，对“原子”操作做出分析，并保证原子操作期间别的线程无法访问竞争资源。
	6、当多个线程等待一个对象锁时，没有获取到锁的线程将发生阻塞。
	7、死锁是线程间相互等待锁锁造成的，在实际中发生的概率非常的小。

# <font size=4>**java synchronized 实例\(安全初始化模式实例\)**

最后安全初始化模式实例下一篇说明



