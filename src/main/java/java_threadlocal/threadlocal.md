java 文档中对ThreadLocal介绍:https://docs.oracle.com/javase/8/docs/api/java/lang/ThreadLocal.html

[toc]

# ThreadLocal类作用
从名称中Local为本地的,局部的意思。api文章中这样定义的：
>This class provides thread-local variables.
 These variables differ from their normal counterparts in that each thread that accesses one
 (via its get or set method) has its own, independently initialized copy of the variable.
 ThreadLocal instances are typically private static fields in classes that wish to associate state with a
  thread (e.g., a user ID or Transaction ID).
 这个类提供线程的局部变量,这些变量不同于普通变量,每个线程都会有这些变量的副本(通过get和set方法).ThreadLocal典型实例:在类中私有
 静态成员变量,表示线程状态字段(例如用户ID或者事物ID).

 java ThreadLocal使得我们可以创建只能由同一个线程读或者写的变量。因此，即使两个线程执行同一个段代码，并且代码都具有对ThreadLocal变量的 引用，那么这两个线程就不能看到彼此ThreadLocal变量。

 ThreadLocal实际上并没有解决共享变量的线程安全问题，它只是把所谓的共享变量变成自己的变量而已。
 概括起来说，对于多线程资源共享的问题，同步机制采用了“以时间换空间”的方式，而ThreadLocal采用了“以空间换时间”的方式。前者仅提供一份变量，让不同的线程排队访问，而后者为每一个线程都提供了一份变量，因此可以同时访问而互不影响。

# 创建一个ThreadLocal
```
private ThreadLocal myThread = new ThreadLocal();

```

实例化一个ThreadLocal对象,对于每个线程,我们只要初始化ThreadLocal一次即可。即使不同的线程执行相同的代码，每个线程都会有他自己的ThreadLocal实例。

即使两个线程设置不同值在相同的ThreadObject，他们也不会互相看到。

# 访问一个ThreadLocal
上述一个ThreadLocal已经被创建，我们可以set变量值，储存里面，如下。
```

myThread.set("A thread local value");
```
可以读取储存在ThreadLocal中的变量值.

```
String threadLocalValue = (String) myThread.get();
```

get()方法返回一个对象,set方法参数为Object.


# 一般使用的ThreadLocal
我们一般这样创建ThreadLocal,以便我们不需要对value进行类型转换,只要直接调用get(),如下例子.

```
 private ThreadLocal<String> myThreadLocal = new ThreadLocal<String>();
```
然后我们可以这样存储value和获取value的值.

```
   myThreadLocal.set("Hello ThreadLocal");
   String threadLocalValue =   myThreadLocal.get();
```

# Initial ThreadLocal Value

在ThreadLocal设置的值,只对当前线程可见.
也可以通过指定初始化值,通过继承和重写initialValue()方法,如下.
```
 private ThreadLocal myThreadLocal = new ThreadLocal<String>(){
           @Override protected String initialValue(){
               return "this is initial value";
           }
       };

```
所有的线程都能有相同的初始值,在他们调用get()在set()前.

# ThreadLocal实例

```
package java_threadlocal;

/**
 * Created by fang on 2017/11/30.
 * ThreadLocal 实例.
 */
public class ThreadLocalExample {
    public static class MyRunnable implements Runnable{

        private ThreadLocal<Integer> threadLocal = new ThreadLocal<Integer>();
        public void run(){
            threadLocal.set((int)(Math.random() * 1000));

            try{
                Thread.sleep(1000);
            }catch (InterruptedException e){

            }

            System.out.println(threadLocal.get());
        }
    }

    public static void main(String[] args) throws InterruptedException{
        MyRunnable myRunnable = new MyRunnable();

        Thread thread1 = new Thread(myRunnable);
        Thread thread2 = new Thread(myRunnable);

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
    }
}


```

# ThreadLocal类中的方法
void set(Object value): 设置当前线程的线程局部变量的值。
public Object get():返回当前线程对应的局部变量。
public void remove(): 将当前线程局部变量值删除,目的为了减少内存的占用.当线程结束后,JVM对线程的局部变量会自动回收,所以显示调用该方法并不是必须的。
protected Object initialValue():返回该线程局部变量的初始值,该方法时projected方法,可以让之类覆盖而设计。
这个方法时延迟调用方法,在线程第一次调用get()或set()时执行,并且只执行一次. ThreadLocal中的缺省实现直接返回null。

# ThreadLocal实现原理
在ThreadLocal类中有一个Map,用于存储每一个线程的变量副本,Map中的key作为线程对象,而值对应的是线程的变量副本.ThreadLocal是空间换时间的思路解决共享变量竞争问题的。

JDK实现ThreadLocal类的类图如下.
![这里写图片描述](http://img.blog.csdn.net/20171130154129911?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvbG92ZXN1bW1lcmZvcmV2ZXI=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)
可以看一下ThreadLocal的源码.
java.lang.Thread
```

 public class Thread implements Runnable
 {
   …
    ThreadLocal.ThreadLocalMap threadLocals = null;
   …
 }
```
 java.lang.ThreadLocal
```
 public class ThreadLocal {
    /************************** SET CASE ****************/
	    public void set(T value) {
	         Thread t = Thread.currentThread();
	         ThreadLocalMap map = getMap(t);
	         if (map != null)
	             map.set(this, value);
	         else
	             createMap(t, value);
	    }

    ThreadLocalMap getMap(Thread t) {
       return t.threadLocals;
    }

    void createMap(Thread t, T firstValue) {
         t.threadLocals = new ThreadLocalMap(this, firstValue);
    }

    /******************** GET CASE ********************/
    public T get() {
         Thread t = Thread.currentThread();
         ThreadLocalMap map = t.threadLocals;
         if (map != null) {
             ThreadLocalMap.Entry e = map.getEntry(this);
             if (e != null)
                 return (T)e.value;
         }
         return setInitialValue();
    }

    private T setInitialValue() {
         T value = initialValue();
         Thread t = Thread.currentThread();
         ThreadLocalMap map = getMap(t);
         if (map != null)
             map.set(this, value);
         else
             createMap(t, value);
         return value;
    }

    protected T initialValue() {
         return null;
    }

    /*************** REMOVE CASE **********/
    public void remove() {
          ThreadLocalMap m = getMap(Thread.currentThread());
          if (m != null)
              m.remove(this);
    }

    ...

    ...
    static class ThreadLocalMap {

       static class Entry extends WeakReference {
          Object value;
          Entry(ThreadLocal k, Object v) {
             super(k);
             value = v;
          }
       }

       private void  set(ThreadLocal key, Object value) {

          Entry[] tab = table;
          int len = tab.length;
          int i = key.threadLocalHashCode & (len-1);

          for (Entry e = tab[i];
             e != null;
             e = tab[i = nextIndex(i, len)]) {

             ThreadLocal k = e.get();
             if (k == key) {
                e.value = value;
                return;
             }

             if (k == null) {
                replaceStaleEntry(key, value, i);
                return;
             }
          }

          tab[i] = new Entry(key, value);
          ...
 }
```


第一次set对象,如果当前线程的ThreadLocalMap不存在,它就会被创建,然后将线程的副本变量设置为Map的value,将ThreadLocal实例作为key。

调用第一次get()的时候,如果当前线程的ThreadLocalMap存在,则使用当前的ThreadLocal实例作为key,取得对应的副本变量。否则为当前线程创建一个新的ThreadLocalMap对象,并设置默认的初始值。



# ThreadLocal内存泄漏
https://www.cnblogs.com/onlywujun/p/3524675.html

参考:http://tutorials.jenkov.com/java-concurrency/threadlocal.html
