package unsafeThread;

/**
 * Created by fang on 2017/11/20.
 */
public class NoVisibility {
    public static boolean ready;
    public static int number;

    private static class ReadThread extends Thread{
        public void run(){
//            while (!ready){
//                Thread.yield();//暂停当前正在执行的线程对象,并执行其他程序.
                System.out.println(ready);
                System.out.println(number);
//            }
        }
    }

    public static void main(String[] args){
        new ReadThread().start();
        number = 42;
        ready = true;
    }
}
