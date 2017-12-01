package thread_create;


/**
 * Created by fang on 2017/12/1.
 * 第二种方式实现Runable接口
 */
public class MyRunnable implements Runnable{

    public void run() {
       System.out.println("MyRunnable running");
    }

    public static void main(String[] args) {
        Thread thread = new Thread(new MyRunnable(),"New Thread");
        thread.start();
        System.out.println(thread.getName());

        // 匿名类实现接口.
        Runnable myRunnable = new Runnable() {
            public void run() {
                System.out.println("Runnable running");
            }
        };
        Thread thread1 = new Thread(myRunnable);
        thread1.start();
    }

}
