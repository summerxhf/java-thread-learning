package thread_create;

/**
 * Created by fang on 2017/11/30.
 * 创建多线程 1 ,继承Thread类 重写run()线程体.
 * 创建子类对象.,调用对象.start()方法.
 */
public class Rabbit  extends Thread{
    @Override
    public void run() {
        //线程体.
        for(int i = 0;i<100;i++){
            System.out.println("兔子跑了" + i + "步");
        }
    }

}
