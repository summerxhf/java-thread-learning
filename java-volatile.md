
>&emsp;&emsp;java volatile 关键字用于将java变量标记为 "被存储在主内存中"。精确的来讲，每次读取被volatile关键字修饰的变量时，都是从计算机的主存中读取，并不是从cup缓存中读取的，并且个volatile变量都是直接写入到主存中，并不是仅仅写入cpu缓存中。

&emsp;&emsp;事实上，从java5开始，volatile关键字不仅仅保证了从主内存中写入和读取volatile变量。从下面几个方面介绍volatile关键字。

[TOC]




#**<font size=4>java volatile可见性保证</font>**
&emsp;&emsp;java volatile关键字保证在每个线程共享该变量。
	
&emsp;&emsp;在多线程应用中，多线程操作没有volatile修饰的变量，每个线程使用他们的时候，复制该变量从主内存到cpu缓存中。在性能方面，如果你的电脑中包含多个cup（我理解还可能是多核），每个线程可能运行在不同的cpu上。每个线程复制不同的cup主内存到对应的cpu缓存中，如下图所示。
	![线程不同的cpu操作](http://img.blog.csdn.net/20171121174625576)

<br>
<br>
&emsp;&emsp;没有被volatile修饰的变量，当从jvm中读数据从主内存到cpu缓存，或者从cup缓存到主内存写数据，是没有保障的（数据的一致性）。可能会导致以下几个问题。
		

&emsp;&emsp;先来张计算机硬件cpu的工作原理图（写入和读都是先从缓存中写和读，之后主存），如下。
![cpu原理](http://img.blog.csdn.net/20171121175241392?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvbG92ZXN1bW1lcmZvcmV2ZXI=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)
&emsp;&emsp;假设两个或者多个线程访问共享对象的情况，对象中有一个成员变量，例如计数器，代码如下。

```
public class SharedObject {

    public int counter = 0;

}
```

&emsp;&emsp;如果thread 1 写入counter数据，thread 1和thread2 随时可以读取counter数据。

&emsp;&emsp;如果counter变量没有声明为volatile变量，不能保证thread1写入变量的值，从cpu缓存中写入到主存中，这意味着cpu中缓存的值可能与主从中的值不一致，如下图所示。

![这里写图片描述](http://img.blog.csdn.net/20171121180657071)

&emsp;&emsp;这个问题是thread2没有及时的看到最新的变量数据，cpu1中的缓存数据还没有回写到主从中，这就是“可见性”问题。一个线程更新数据，其他线程可能看不到这个数据。

&emsp;&emsp;当我们把变量定义为volatile变量类型,则所有的写操作就会即可被写入到主存中。并且，所有的读取counter变量，将会从主存中读取。如下代码：

```
public class SharedObject {

    public volatile int counter = 0;

}
```
&emsp;&emsp;定义volatile变量，保证了其他线程对该变量的可见性。

----------

#**<font size=4>java volatile Happens-Before (先行发生)Guarantee</font>**
从java5开始，volatile关键字不仅保证了变量从内存中读取和写入。实际上，volatile关键字也保证了如下：

 1. 如果线程A写入一个volatile变量，随后线程B读取相同的变量。那么变量对线程A来说在写入变量前就是可见的，对于B来说读取完变量后，对该变量也是可见的。
 <br>
 2. volatile变量的读和写指令不能由JVM重新排序（）。读写指令前后可以重排序，但是volatile读和写不能与这些指令混合。无论什么指令都应该在volatile变量读写之后。


&emsp;&emsp;深刻的理解：
&emsp;&emsp;当一个线程去写入一个volatile变量，不仅仅是volatile变量本身被写入内存。在写入volatile变量之前，所有其他变量也会被刷新到主存中。
&emsp;&emsp;当一个线程读取一个volatile变量时，他还会读取和volatile变量一起被刷新到主存中的变量。如下伪代码：

```
Thread A:
    sharedObject.nonVolatile = 123;
    sharedObject.counter     = sharedObject.counter + 1;

Thread B:
    int counter     = sharedObject.counter;
    int nonVolatile = sharedObject.nonVolatile;
```

 &emsp;&emsp;线程A在写入volatile变量counter之前写入一个没有volatile修饰的变量nonVolatile。sharedObject.nonVolatile 和sharedObject.counter 变量都被写入到主存中,当线程写入被volatile修饰的变量counter。
<br>
&emsp;&emsp;线程B读取sharedObject.counter 和sharedObject.nonVolatile,都会从主存中读取到cpu缓存中。并且线程B在读取sharedObject.nonVolatile的值时，是可以看到被线程A写入的sharedObject.nonVolatile值。
<br>
&emsp;&emsp;开发人员可以使用这种扩展的可见性保证,来优化线程之间变量的可见性,不需要每个变量都声明为volatile, 只需要一个或几个变量声明即可。如下代码示例：

```
public class Exchanger {

    private Object   object       = null;
    private volatile hasNewObject = false;

    public void put(Object newObject) {
        while(hasNewObject) {
            //wait - do not overwrite existing new object
        }
        object = newObject;
        hasNewObject = true; //volatile write
    }

    public Object take(){
        while(!hasNewObject){ //volatile read
            //wait - don't take old object (or null)
        }
        Object obj = object;
        hasNewObject = false; //volatile write
        return obj;
    }
}
```
&emsp;&emsp;线程A可以通过调用put（）方法来存入对象。线程B可以通过调用take（）方法来获取对象。Exchanger类可以很好的使用volatile变量（而没有使用synchronized 块），只要线程A调用put（）方法并且线程B调用take（）方法。
&emsp;&emsp;然后JVM为了优化性能，可能会对指令进行重排列，JVM在put（）和take（）切换读写顺序将会发生什么？可能之前的代码顺序变成如下：

```
while(hasNewObject) {
    //wait - do not overwrite existing new object
}
hasNewObject = true; //volatile write
object = newObject;
```
&emsp;&emsp;对volatile变量的写操作在新对象new之前执行。对于JVM来说是完全可能的，这两个写的指令并不相互依赖。
&emsp;&emsp;重排列指令将损害变量的可见性。首先，线程B可能看到hasNewObject 变量设置为true，在线程A真正的去new一个对象变量之前。其次，不能保证写入的对象将会被刷新到内存中（而线程A写入hasNewObject 为true）。
&emsp;&emsp;为了防止上述情况的发生，volatile关键字保证了“在保证之前发生”。在对保证volatile变量的读写指令不能重排序之前发生情况。指令前后的指令可以被重排序，但是volatile的读写指令不能被重排序，任何指令只能在他之前或者之后发生。如下栗子。

```
sharedObject.nonVolatile1 = 123;
sharedObject.nonVolatile2 = 456;
sharedObject.nonVolatile3 = 789;

sharedObject.volatile     = true; //a volatile variable

int someValue1 = sharedObject.nonVolatile4;
int someValue2 = sharedObject.nonVolatile5;
int someValue3 = sharedObject.nonVolatile6;
```
&emsp;&emsp;JVM可以重新排列前三条指令，只要他们都发生在volatile写指令之前。（必须在volatile写指令之前执行）
&emsp;&emsp;类似，如果volatile指令在前，JVM可能会重排序后面的3条指令，在执行volatile指令之前，最后的3条指令都不能被重排序。

&emsp;&emsp;以上基本就是java volatile Happens-Before （先行发生）Guarantee的意义。

----------


#**<font size=4>volatile is not always enough</font>**
&emsp;&emsp;即使volatile关键字保证变量读和写都直接从主存中读写,但是仍然有一些情况下,声明volatile变量是不足够的。
&emsp;&emsp;在前面的解释中，一个线程写入了volatile共享计数器变量，volatile能够保证线程2总是看到的是最新的值。	
&emsp;&emsp;实际上多个线程可以写入一个共享volatile变量中,并且正确的存储在主存中，前提是这个变量的新值不依赖于他之前的值。换句话说，如果一个线程写入volatile变量的值，就不要先读取他的值来计算他的下一个值。
&emsp;&emsp;只要一个线程首先要读取volatile变量的值,并且基于该值为共享volatile变量生成一个新的值, 则volatile变量则不足以保证其可见性的正确性。正在读和正在写之间有一个极短时间的空隙，可能会存在竞争条件，多线程可能读取volatile变量相同的值，为变量生成一个新的值，当写回主存的时候会彼此覆盖自己的值。

&emsp;&emsp;所以说像上述情形，volatile变量并不能保证线程安全，可以进一步分析。

&emsp;&emsp;虽然说被volatile标记的变量会写入到主存中，但并不代表不会写入到cpu缓存中，和普通变量比起来，同样是先写入到cpu缓存中，然后同时立即同步到主存中。而普通变量，是先写入到cpu缓存中，但不会立即同步到主存中。所以不能保证其他线程即使见到执行结果。
&emsp;&emsp;如果线程1读取主存中counter变量为0，并且增加counter到1写入到cpu缓存中，在还没有写入到主存的时候。线程2有也可以读取相同的counter变量从主存中，并且counter变量的值仍是0，并且增加1，写入到自己的cpu缓存中，也还没有写回到主存中。这种情形如下图所示：
![多线程volatile变量的不安全](http://img.blog.csdn.net/20171122194632086?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvbG92ZXN1bW1lcmZvcmV2ZXI=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

&emsp;&emsp;事实上两个线程是不同步的，但是真正的共享变量的值应该是2。但每个线程在他们自己cpu缓存中变零counter值都是1，在主存中仍然是0。两个线程将共享的计数器变量counter写入到主存中，可能其中的一个都会被覆盖，最终主存中的值为1，但是这个值是错误的值。


----------

#**<font size=4>when is volatile enough?</font>**
&emsp;&emsp;正如前面提到的如果两个线程都在读取和写入一个共享变量，那么使用volatile关键字是不够的。在这种情况下，我们需要使用同步来保证变量的读取和写入是原子的。读取和写入volatile变量不会阻塞线程的读和写。为了这一点，我们需要使用synchronized关键字。
&emsp;&emsp;可以用java.util.concurrent 包中的原子数据类型来替换synchronized块，例如AtomicLong或者AtomicReference等等。
>&emsp;&emsp;In case only one thread reads and writes the value of a volatile variable and other threads only read the variable, then the reading threads are guaranteed to see the latest value written to the volatile variable. Without making the variable volatile, this would not be guaranteed.
&emsp;&emsp;如果只有一个线程读和写volatile变量，并且其他的线程只是读这个变量值，读取的线程就能看到最新写入的volatile变量值。如果没有用volatile变量，这将不能保证。
&emsp;&emsp;The volatile keyword is guaranteed to work on 32 bit and 64 variables.
&emsp;&emsp;volatile关键字可以在32位和64位变量上起作用.

 ----------

# **<font size=4>volatile 性能考虑</font>** #
&emsp;&emsp;读和写volatile变量将会从主存中读和写。读取和写入主存中要比读取和写入cpu缓存要昂贵的多。访问volatile变量可以防止JVM重排序，一般的性能增强技术，因此当真正需要增加变量的可见性时，我们
需要使用volatile关键字。

----------

#**<font size=4>summary</font>**
&emsp;&emsp;第一次使用markdown编辑器写文章感觉不错，哇咔咔。算是初步接触volatile关键字，大概了解，还是要多实践才会深刻。不能一口吃个胖子，都要和老黄牛一样，遇到了时不时地反复咀嚼。


&emsp;&emsp;<font size=2>参考原文：http://tutorials.jenkov.com/java-concurrency/volatile.html</font>