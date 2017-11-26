>安全发布对象的安全性都来自于JMM提供的保证,而造成不正确的发布原因,就是在"发布一个共享对象"与"另一个线程访问该对象"之间缺少一种Happen-Before排序.
[TOC]

# 不安全的发布


```
package safe_unsafe_publication;
/**
 * Created by fang on 2017/11/24.
 * 不安全发布,一个线程正在初始化,另一个线程可能读到未构造完全的对象.
 */
public class UnsafeLazyInitialization {
    private static Resource resource;

    public static Resource getInstance(){
        if(resource == null){
            resource = new Resource();
        }
        return resource;
    }
}

```
上述代码中的不安全性有两个方面
1 资源竞争问题
类的静态变量存在方法区,resource类变量("地址变量"),被线程A 和线程B共享,但是当线程A写入resource地址变量值的时候,可能在cpu寄存器(cpu高速缓存)还未同步到cpu主存中(每一个线程都有自己的cpu高速缓存,在高速缓存中互相看不到).

导致线程B在判断resource仍然为null,线程B可能也会new Resource()操作. 这就导致了资源的竞争,也就是(查看--修改--写入)并非是原子性操作.

2 类的实例对象构造不完整,或构造错误问题.
线程A在实行getInstance()方法的时候,发现resource为null,然后new Resource(),然后设置为指向resource.
 然后线程B在判断resource是否为null时,假设此时线程A中的类变量resource已经同步到CPU主存,B发现类变量resource并不为null,然后就获取resource地址,直接使用线程A new出来的对象.
 .B可能使用A创建的Resource对象并不完整,或者创建时错误的.(线程B看到A线程的执行顺序,和A真正的执行顺序可能不同,JVM重排列).

(以上是自己的理解,**与类的加载过程有关系**,在加载 连接阶段已经有了类的地址变量,但下一个阶段才是对象的初始化阶段. 也就是在方法区已经存在对象实例的地址对象变量,但是堆中的实例对象并没有初始化完整.)




官方说明:
>除了不可变对象外,使用被另一个线程初始化的对象通常是不安全的,除非对象的发布操作是在使用该对象的线程开始使用之前执行.


# 安全发布
## 安全初始化模式
如下代码
```
 package safe_unsafe_publication;


 /**
  * Created by fang on 2017/11/24.
  * 安全的延迟加载
  */
 public class SafeLazyInitialization {
     private static Resource resource;

     public synchronized static Resource getInstance(){
         if(resource ==null){
             resource = new Resource();
         }
         return resource;
     }
 }

```
在类的方法上添加同步锁,synchronized一方面解决了非原子性问题,又解决了可见性问题,并且不会引发JVM重排列,解决了第一个例子中的两个问题.
但是多线程每次都要调用这个方法,得到对象的引用,当访问量十分大的时候,每次调用这个方法获得锁和释放锁
的开销似乎是不必要的: 一旦初始化完成获得锁和释放锁就显得没有那么必要,可以用提前初始化方式进行优化.

## 提前初始化
代码如下,可以定义静态成员变量,直接初始化Resource类.
```package safe_unsafe_publication;

   /**
    * Created by fang on 2017/11/24.
    * 提前初始化方式.
    */
   public class EagerInitialzation {
       private static Resource resource = new Resource();

       public static Resource getResource(){
           return resource;
       }
   }
```
避免了每次调用getInstance时所产生的同步开销.
通过这项技术和JVM的延迟加载技术结合起来,可以形成一种延迟初始化技术, 这样在代码中就不需要同步.(与类的加载有关,当类有static修饰时,读取静态字段的时候,
虚拟机会对类进行"初始化",且JVM保证了静态类初始化且赋值的时候的原子性和可见性,所以无需我们显示的同步.)
>采用特殊的初始化方式来处理静态域,并提供了额外的线程安全保证. 静态初始化器是由JVM在类的初始化阶段执行,即类加载后并且被线程使用之前。
JVM在初始化期间获得一个锁,并且每个线程都至少获取一个这个锁,以确保这个类已经加载,因此在静态初始化期间,内存写入操作将自动对所有线程可见.
因此无论是构造期还是被引用时,静态初始化对象都不需要显示的同步.

这个规则仅仅适用于构造时状态,如果对象是可变的,那么在读取线程和线程指尖仍然需要通过同步来确保修改操作是可见的,以及避免数据被破坏.

## 延迟初始化占位
```
package safe_unsafe_publication;

/**
 * Created by fang on 2017/11/24.
 *  延长初始化占位符.
 */
public class ResourceFactory {
    public static class ResourceHolder{
        public static Resource resource = new Resource();
    }

    public static Resource getResource(){
        return ResourceHolder.resource;
    }
}

```
上个例子中的提前初始化,可能在加载对象EagerInitialzation时,就会立刻初始化static变量修饰的Resource.我们想要做到在使用它时再进行初始化.
使用专门的ResourceFactory类初始化Resource.JVM推迟ResourceHolder初始化操作,直到使用这个类才初始化.(https://docs.oracle.com/javase/specs/jls/se8/html/jls-12.html#jls-12.4.1:初始化发生情况)
当执行getResource()方法时,都会使ResourceHolder被加载和被初始化,此时静态初始化器将会执行Resource的初始化操作.

## 双重检查锁初始化(DCL)
```package safe_unsafe_publication;

   /**
    * Created by fang on 2017/11/25.
    * 双重检查锁初始化DCL
    */
   public class DoubleCheckedLocking {

       private static Resource resource;

       public static Resource getInstance(){
           if(resource == null){
               synchronized (DoubleCheckedLocking.class){
                   if(resource == null){
                       resource = new Resource();
                   }
               }
           }
           return resource;
       }
   }
```
和安全初始化模式比,在原来的基础上,将synchronized在方法上改为同步到DoubleCheckLocking这个类上.
避免了无论Resource是否被实例化都要获取锁和释放锁操作,这样只有resource == null时才获取这个锁,并进行同步判断.
在资源竞争方面线程是安全的,但是在可见性方面或者说类的构造是否完整方面,就会有 例子"不安全发布中的问题2",当这个类的类变量("地址变量")已经存在但new出来的时候未完全加载成功的时候,另一个线程可能就会使用这个
错误的对象或者是无效的对象(与类的加载机制有关,区分实例化一个类和实例化一个对象.).
怎么解决类的不完全构造和jvm重排列问题呢?
如下代码,在定义的时候添加上volatile关键字这会解决这个上述问题,因为volatile变量,控制了JVM的重排列,并且两个线程都可以在内存中可见.

(volatile关键字:http://blog.csdn.net/lovesummerforever/article码,在定义的时候添加上volatile关键字这会解决这个上述问题,因为volatile变量(volatile关键字:http://blog.csdn.net/lovesummerforever/article/details/78603856)
```
package safe_unsafe_publication;

/**
 * Created by fang on 2017/11/25.
 * 双重检查锁初始化DCL
 */
public class DoubleCheckedLocking {

    private static volatile Resource resource;

    public static Resource getInstance(){
        if(resource == null){
            synchronized (DoubleCheckedLocking.class){
                if(resource == null){
                    resource = new Resource();
                }
            }
        }
        return resource;
    }
}

```

# 初始化过程中的安全性
如果Resource是不可变的,或者我们在使用过程中不需要改变Resource,我们可以用final来修饰,确保了Resource的原子性和可见性.

在java内存模型中,final域能确保初始化过程的安全性,从而可以不受限制的访问不可变对象,并共享这些对象的时候无需同步.
如下代码
```package safe_unsafe_publication;

   import java.util.HashMap;
   import java.util.Map;

   /**
    * Created by fang on 2017/11/26.
    * final保证可见性和原子性.
    */
   public class SafeStates {
       private final Map<String,String> states;

       public SafeStates(){
           states = new HashMap<String, String>();
           states.put("akasja","AK");
           states.put("alabama","AL");//and so on
       }

       public String getAbbreviation(String s){
           return states.get(s);
       }
   }
```

对于final类的对象,初始化安全性可以防止对对象的初始引用被重排列到构造过程之前. 当构造函数完成时,
构造函数对final域的所有写入操作,以及对通过这些域可以到达的任何变量的写入操作,都将被"冻结", 并且任何获得该对象
引用的线程都至少能确保看到被冻结的值.
对于final域可到达的初始变量的写入操作,将不会与构造过程的操作一起呗重排序.

>初始化安全性只能保证通过final域可达的值从构造过程完成时开始的可见性. 对于通过非final域可达的值,
或者在构造完成后可能改变的值, 必须通过同步来确保可见性.

# summay
对线程的可见性和原子性有了一定的了解,对非安全发布和安全发布有一些自己的理解,目前理解为这个程度,可能以后回来看会有新的理解.
上述的一些观点和理解有问题,欢迎交流~
这篇文章涉及到了JVM类的加载和java内存模型以及JVM的重排列,下面文章一一解释.
