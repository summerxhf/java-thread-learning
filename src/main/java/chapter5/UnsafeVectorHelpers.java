package chapter5;

import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: XINGHAIFANG
 * Date: 2019/7/15
 * Time: 10:31
 * 两个线程,一个添加,一个删除;
 * 无论是否加synchronized都可能
 *
 * 一个线程可能先执行最后一个元素 9下标的删除, 另外一个线程正在读取9下标的数据,导致抛出java.lang.ArrayIndexOutOfBoundsException异常
 */
public class UnsafeVectorHelpers {
    public static  Vector list = new Vector();
    public static Object getLast(Vector list){
        synchronized (list){
            int lastIndex = list.size()-1;
            return list.get(lastIndex);
        }

    }

    public static void deleteLast(Vector list){
        synchronized (list){
            int lastIndex = list.size()-1;
            list.remove(lastIndex);
        }

    }
    public static class MyThread extends Thread{
        public void run(){

            UnsafeVectorHelpers.getLast(list);
        }

    }
    public static class MyThread2 extends Thread{
        public void run(){
            UnsafeVectorHelpers.deleteLast(list);
        }

    }

    public static void main(String[] args) {
        //两个线程, 一个先上添加一个线程删除;
        list.add(new Object());
        MyThread myThread = new MyThread();
        MyThread2 myThread2 = new MyThread2();
        myThread.start();
        myThread2.start();

        System.out.println("3434");
    }
}
