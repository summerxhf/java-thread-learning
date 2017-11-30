package thread_create;

/**
 * Created by fang on 2017/11/30.
 *
 */
public class RabbitApp {
    //main方法也是一个线程,还有gc线程,还有异常线程,以及我们新建的两个类的线程.
    public static void main(String[] args) {
        Rabbit rabbit =  new Rabbit();
        rabbit.start();//程序内部调用run()方法了.

        Tortoise tortoise = new Tortoise();
        tortoise.start();//使用start开启多个路径,如果直接调用run()就不会交错显示.

        for(int i=0;i<100;i++){
            System.out.println("main-->" + i);
        }

        //输出结果并没有按照程序代码步骤输出,而是一起进行的.这就是多线程执行.

    }

}
