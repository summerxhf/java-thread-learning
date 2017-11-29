[toc]

# <font size="4">**首先是什么是java内存模型？**

&emsp;&emsp;不同的操作系统有不同的内存模型，“内存模型”一词可以理解为在特定操作写一下，对特定的内存或者高速缓存进行读写访问的抽象过程。
&emsp;&emsp;不同的物理机有不同的内存模型。而java内存模型是来屏蔽掉各种不同物理机及其不同操作系统的内存访问差异，以实现java程序在各种平台下都能达到一致的访问效果。
&emsp;&emsp;java 内存模型解释了java虚拟机是如何与计算机内存(RAM)工作的。
&emsp;&emsp;下面先了解一下计算机硬件的内存模型。


# <font size="4">**计算机硬件内存模型**
&emsp;&emsp;为了更充分的利用计算机处理器的效能，当处理器需要与内存交互，需要读取运算数据、存储运算结果等，这些IO操作是很难消除的。
&emsp;&emsp;由于计算机的存储设备和处理器的运算速度有几个数量级的差距，所以现代计算机系统不得不加入一层读写速度尽可能接近处理器运算速度的高速缓存来作为内存与处理器之间的缓冲：将运算需要使用的数据复制到缓存中，让运算可以快速进行，当运算结束后再从缓存同步回内存之中，这样处理器就无须等待缓慢的内存读写了。如下图是计算机硬件内存模型。<br>
![计算机内存模型](http://img.blog.csdn.net/20171129151345164?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvbG92ZXN1bW1lcmZvcmV2ZXI=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)
&emsp;&emsp;现代计算机一般是2个或者更多cpu,或者一个cpu有多核,这样就可以同时运行多个线程。

&emsp;&emsp;java内存模型和计算机硬件内存模型类似，java虚拟机也有自己的内存模型。

&emsp;&emsp;处理器可能会对输入的代码乱码执行优化，处理器会对乱序执行的结果重组，保证该结果与顺序执行的结果是一致的。但并不保证程序中各个语句计算的先后顺序与输入代码中的顺序一致，因此如果在一个计算机任务依赖另外一个计算任务中间结果，那么其顺序性不能靠代码先后顺序来保证。类似的，java虚拟机即时编译器也有类似指令重排序的优化。


# <font size="4">**java内存模型**
&emsp;&emsp;jdk1.5后java内存模型逐渐完善。
&emsp;&emsp;java内存模型主要目标是定义程序中各个变量的访问规则，即在虚拟机中将变量存储到内存和从内存中取出变量的底层细节。
&emsp;&emsp;这里的变量不包括局部变量与方法参数，他们为线程私有。这里的内存模型中的变量包括实例字段、静态字段和构成数组对象的元素，JMM对他们的管理。
java内存模型规定了所有变量（实例字段、静态字段和构成数组对象的元素），都在自己的主内存中。每个线程都有自己的工作内存，线程对变量的所有操作都必须在工作内存中进行，而不能直接读取主内存中的变量。线程工作空间保存了该线程使用的变量的副本拷贝。(ps:这里的主存是jvm虚拟机主存,是计算机主存的一部分)线程间变量之间的传递需要通过主内存来完成。
如下图所示

![java内存模型](http://img.blog.csdn.net/20171129152718099?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvbG92ZXN1bW1lcmZvcmV2ZXI=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)


&emsp;&emsp;这与之前内存结构有什么连接呢?可以说是从两个方面对jvm内存不同层次的划分,java内存模型是从"逻辑角度"出发,而把jvm内存划分为
&emsp;&emsp;堆和栈等是从结构方面的划分,我认为的联系如下下图。看问题的横向和纵向。
![不同层次的内存划分](http://img.blog.csdn.net/20171129152919221?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvbG92ZXN1bW1lcmZvcmV2ZXI=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

# <font size="4">**java虚拟机运行时数据区**

&emsp;&emsp;线程隔离数据区（每个线程私有）的部分包括，虚拟机栈、本地方法栈、程序计数器。
&emsp;&emsp;所有线程共享的部分包括方法区和堆。其中

## <font size="3">**1、程序计数器**

&emsp;&emsp;程序计数器，是一块较小的空间，它可以看做当前线程所执行的字节码的行号指示器，字节码解释器工作时就是通过改变计数器的值来选取下一条需要执行的字节码指令。
&emsp;&emsp;每个线程有独立的计数器，让各个线程互不影响，独立存储，是“线程私有内存”。java虚拟机规范中是唯一一个没有规定任何OutOfMemoryError情况的区域。



## <font size="3">**2、 虚拟机栈**
&emsp;&emsp;和程序计数器一样，是线程私有的，它的生命周期与线程相同。
&emsp;&emsp;虚拟机栈是描述java方法执行的内存模型：每个方法在执行的同时都会创建一个栈帧用于存储局部变量、操作数栈、动态链接、方法出口等信息。每个方法从调用直至完成的过程，就对应着一个栈帧在虚拟机中入栈到出栈的过程。
&emsp;&emsp;局部变量存放的是各种基本数据类型（boolean、byte、char、short、int、float、long、double）、对象引用类型。

&emsp;&emsp;这个区域规定了两种异常情况：1 如果线程请求栈的深度大于虚拟机所允许的深度，将抛出StackOverflowError异常。2 如果是虚拟机栈扩展时无法申请到足够空间时，会抛出
OutOfMemoryError异常。

## <font size="3">**3、 本地方法栈**
&emsp;&emsp;与虚拟机栈类似，不同的是是为虚拟机使用的Native方法服务。与虚拟机栈一样，也会抛出StackOverflowError和OutOfMemory异常。

线程共享数据区，对和方法区
## <font size="3">**4、 堆**
&emsp;&emsp;java堆是java虚拟机内存中最大的一块。java对是被所有线程共享的一块区域，在线程启动时创建，这个区的唯一目的就是存放对象实例以及数组都会在堆上分配。
&emsp;&emsp;当前虚拟机都是按照可扩展实现的（通过-Xmx和-Xms）如果在堆中没有完成实例分配，并且堆也无法扩展时，将会抛出OutOfMemoryError异常。

## <font size="3">**5、 方法区**
&emsp;&emsp;虽然叫方法区，但是存放的是类的信息，常量，静态变量、即时编译器编译后的代码等数据。
&emsp;&emsp;与java堆一样各个线程共享的内存区域。
&emsp;&emsp;当方法区无法满足内存需要时，将抛出OutOfMemoryError异常。

&emsp;&emsp;可以把线程共享区java虚拟机栈、计数器空间以及本地方法栈统一说成线程的栈（线程的私有空间），把方法区和堆（线程共享区）表示为堆，java内存的逻辑视图可以表示如下图：
![JMM](http://img.blog.csdn.net/20171129154342872?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvbG92ZXN1bW1lcmZvcmV2ZXI=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

线程非共享区
&emsp;&emsp;JVM每个线程都有自己的线程栈，线程栈包含了当前线程执行的方法调用相关信息，我们也把它称为调用栈。随着代码的变化，调用栈会不断变化。

&emsp;&emsp;线程栈还包含了当前方法的所有局部变量，一个线程只能读取自己的线程栈，也就是说，线程中的本地变量对其他线程是不可见的。
&emsp;&emsp;即使两个线程执行的是同一段代码，他们也会各自在自己的线程栈中创建本地变量，因此每个线程有自己的本地变量版本。

&emsp;&emsp;所有的基础类型（boolean、byte、short、char、int、long、float、double）的变量都将直接保存在线程栈中，对于它们的值各个线程之间是相互独立的。
&emsp;&emsp;基础类型的局部变量，一个线程可以传递一个副本给另一个线程，但是它们之间是无法共享的。

&emsp;&emsp;局部变量无论是原始类型还是对象的引用，都会放到栈中。（对象本身在堆中）

&emsp;&emsp;对象的成员方法，方法中含有局部变量，则需要存储在栈区，即使他们所属的对象在堆中。


线程共享区
&emsp;&emsp;堆中包含了java应用创建对象的所有对象信息，无论是哪个线程创建的，都会放到堆中。也包括原始类型的封装，也就是包装类型。
&emsp;&emsp;不管对象属于一个成员变量还是方法中的本地变量，都会被存储在堆中。
![变量的存储](http://img.blog.csdn.net/20171129154843692?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvbG92ZXN1bW1lcmZvcmV2ZXI=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)


&emsp;&emsp;对于一个对象的成员变量，不管是原始类型还是包装类型，都会被存储在堆中。
&emsp;&emsp;static类型变量以及类本身相关信息都会随着类本身存储在堆中。

&emsp;&emsp;堆中的堆对象可以被多个线程共享。如果一个线程获得对象的应用，它便可以访问对象的成员变量。如果两个线程同时调用了对象的同一个方法，
&emsp;&emsp;那么两个线程便可以同时方法对象的成员变量，但是对于局部变量，每个线程则会拷贝一份到自己的线程栈中。下面的图展示了如下描述：

![类中的成员变量和方法中的变量](http://img.blog.csdn.net/20171129154922592?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvbG92ZXN1bW1lcmZvcmV2ZXI=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

![内存](http://img.blog.csdn.net/20171129161341364?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvbG92ZXN1bW1lcmZvcmV2ZXI=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

# <font size="4" >**java内存模型与计算机内存之间的连接**


&emsp;&emsp;计算机硬件内存架构和java内存模型关系，如下图：

![JMM与计算机内存](http://img.blog.csdn.net/20171129154553491?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvbG92ZXN1bW1lcmZvcmV2ZXI=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)


当对象和变量存储到计算机各个内存区域时,必然会面临一些问题。

# <font size="4" >**原子性、可见性与有序性**
&emsp;&emsp;java内存模型围绕着并发过程中如何处理原子性、可见性和有序性这三个特征来简历的，分别如下。
## <font size="3" > **1.原子性**
&emsp;&emsp;原子性(Atomicity):由JMM直接保证原子性变量操作包括读、加载、赋值、使用、存储和写入，基本数据类型的读写是具有原子性的。
更大范围的原子，JMM提供了lock和unlock操作来满足需求，尽管JVM没有把lock和unlock操作直接开放给我们，但是提供了更高层的指令monitorenter和
monitorexit，反应在java代码中也就是synchronized关键字，synchronized块之间的操作具有原子性。
## <font size="3" > **2.可见性**
&emsp;&emsp;可见性是指一个线程修改了共享变量的值，其他线程能立即知道这个修改。JMM是通过每个线程自己空间中拥有变量副本,修改后将新值同步回主内存,
变量的读取依赖于主存刷新变量,普通变量和volatile变量都是如此,volatile变量与普通变量区别是,volatile保证了新值可以立即同步到主内存中,以及每次
使用前立即从主内存刷新,这种可见性,普通变量不能保证。

&emsp;&emsp;除了volatile外，java还有两个关键字synchronized和final也可以实现可见性。synchronized块的可见性是“对于一个变量执行unlock操作之前，必须先把此变量同步回主内存中”，
而final关键字可见性是指，被final修饰的字段构造器一旦初始化完成，并且构造器并没有把“this”引用传递出去，那在其他线程中就能看到final字段的值。
## <font size="3" > **3.有序性**
&emsp;&emsp;java程序的有序性总结为，如果在本线程内观察，所有的操作都是有序的；如果在一个线程中观察另一个线程，所有操作都是无序的。
前半句是“线程内表现的串行语句”，后半句“指令重排”现象和“工作内存与主内存同步延迟”现象。
&emsp;&emsp;java volatile和synchronized保证了线程的有序性,禁止指令重排列语义,而synchronized决定了持有同一个锁的两个同步块只能串行输入。
### <font size="3" > *指令重排**
>在执行程序时,为了提高性能,编译器和处理器会对指令做重排序。但是，JMM确保在不同的编译器和不同的处理器平台之上，通过插入特定类型的Memory
Barrier（栅栏）来禁止特定类型的编译器重排序和处理器重排序，为上层提供一直的内存可见性保证。

&emsp;&emsp; 1、编译器优化重排序：编译器在不改变单线程程序语义的前提下，可以重新安排语句的执行顺序。
&emsp;&emsp;2、指令级并行的重排序：如果不存在数据依赖性，处理器可以改变语句对应机器指令的执行顺序。
&emsp;&emsp;3、内存系统的重排序：处理器使用缓存和读写缓存，这使得加载和存储操作上看上去可能是乱序执行。

JVM指令重排序更多详情:http://blog.csdn.net/lovesummerforever/article/details/78667955

**数据依赖性**
&emsp;&emsp;如果两个操作访问同一个变量,其中一个为写操作,此时这两个操作之间存在数据依赖性。
&emsp;&emsp;编译器和处理器不会改变存在数据依赖性关系的两个操作的执行顺序,即不会重排序。
**as-if-serial**
&emsp;&emsp;不管怎么重排序，单线程下的执行结果不能被改变，编译器、rentime和处理器都必须遵守as-if-serial。

### <font size="3" > *Happen-before原则 (先行发生原则)**


&emsp;&emsp;程序次序规则：一个线程内，按照代码顺序，书写在前面的操作先行发生于书写在后面的操作。
&emsp;&emsp;锁定规则：一个unLock操作先行发生于后面对同一个锁额lock操作。
&emsp;&emsp;volatile变量规则：对一个变量的写操作先行发生于后面对这个变量的读操作。
&emsp;&emsp;传递规则：如果操作A先行发生于操作B，而操作B又先行发生于操作C，则可以得出操作A先行发生于操作C。
&emsp;&emsp;线程启动规则：Thread对象的start()方法先行发生于此线程的每个一个动作。
&emsp;&emsp;线程中断规则：对线程interrupt()方法的调用先行发生于被中断线程的代码检测到中断事件的发生。
&emsp;&emsp;线程终结规则：线程中所有的操作都先行发生于线程的终止检测，我们可以通过Thread.join()方法结束、Thread.isAlive()的返回值手段检测到线程已经终止执行。
&emsp;&emsp;对象终结规则：一个对象的初始化完成先行发生于他的finalize()方法的开始。
&emsp;&emsp;这8条原则摘自《深入理解Java虚拟机》。

# <font size="4">**总结**
&emsp;&emsp;看了一些书和一些文章就先理解到这，有些是书上的，有些是自己理解的内容，欢迎交流和指正。
<font size="2">参考：http://tutorials.jenkov.com/java-concurrency/java-memory-model.html</font>



