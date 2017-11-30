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

