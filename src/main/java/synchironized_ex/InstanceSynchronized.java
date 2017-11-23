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
