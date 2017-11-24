>安全发布对象的安全性都来自于JMM提供的保证,而造成不正确的发布原因,就是在"发布一个共享对象"与"另一个线程访问该对象"之间缺少一种Happen-Before排序.

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
上述代码中不仅存在资源竞争问题,而且还可能出现如下情形.
线程A在实行getInstance()方法的时候,发现resource为null,然后new Resource.
然后设置为指向resource. 然后线程B在判断resource是否为null时,可能判断并不为null,但是B线程看到A线程的执行顺序,和A真正的执行顺序可能会不同,
可能线程A先指向了resource的引用,然后又修改了堆中new Resource的东西.
所以造成B在使用指向resource的时候,可能该实例处于非"健康"状态.(自己的理解,**与类的加载过程有关系**,在加载 连接阶段已经有了类的地址变量,但下一个阶段才是对象的初始化阶段)

官方说明:
>除了不可变对象外,使用被另一个线程初始化的对象通常是不安全的,除非对象的发布操作是在使用该对象的线程开始使用之前执行.



