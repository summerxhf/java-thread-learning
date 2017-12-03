package deadlock;

import java.util.concurrent.SynchronousQueue;

/**
 * Created by fang on 2017/12/3.
 * 过多的同步方法可能造成死锁.
 */
public class ThreadDeadLock {
    public static void main(String[] args) {
        Object g = new Object();
        Object m = new Object();

        Test test1 = new Test(g,m);
        Test2 test2 = new Test2(g,m);
        Thread proxy = new Thread(test1);
        Thread proxy2 = new Thread(test2);
        proxy.start();
        proxy2.start();

    }
}

class Test implements Runnable{

    Object goods;
    Object money;
    public Test(Object goods,Object money){
        super();
        this.goods = goods;
        this.money = money;
    }
    public void test(){
        synchronized (goods){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (money){
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("一手给钱");
    }
    public void run() {

        while (true){
            test();
        }

    }
}

class Test2 implements Runnable{


    Object goods;
    Object money;
    public Test2(Object goods,Object money){
        super();
        this.goods = goods;
        this.money = money;
    }

    public void test(){
        synchronized (money ){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (goods){
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("一手给货");
    }
    public void run() {

        while (true){
            test();
        }

    }
}
