package chapter5;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: XINGHAIFANG
 * Date: 2019/7/15
 * Time: 16:19
 */
public class CopyOnWriteArrayListDemo extends Thread{
    static CopyOnWriteArrayList copyOnWriteArrayList = new CopyOnWriteArrayList();

    @Override
    public void run() {
        copyOnWriteArrayList.add("D");
    }

    public static void main(String[] args) throws InterruptedException{
        copyOnWriteArrayList.add("A");
        copyOnWriteArrayList.add("B");
        copyOnWriteArrayList.add("C");

        CopyOnWriteArrayListDemo demo= new CopyOnWriteArrayListDemo();
        demo.run();
        Thread.sleep(1000);

        Iterator iterator = copyOnWriteArrayList.iterator();
        while (iterator.hasNext()){
            String s = (String) iterator.next();
            System.out.println(s);
            Thread.sleep(1000);
        }

        //
        for (int i=0;i<copyOnWriteArrayList.size();i++){
            System.out.println(copyOnWriteArrayList.get(i));

        }
        System.out.println(copyOnWriteArrayList);
    }
}
