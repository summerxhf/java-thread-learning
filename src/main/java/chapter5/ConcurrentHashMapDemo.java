package chapter5;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by IntelliJ IDEA.
 * User: XINGHAIFANG
 * Date: 2019/7/15
 * Time: 15:52
 */
public class ConcurrentHashMapDemo {
    private final ConcurrentHashMap<Integer,String> concurrentHashMap = new ConcurrentHashMap<Integer, String>();

    class WriteThreasOne implements Runnable{
        public void run() {
            for(int i=1;i<10;i++){
                concurrentHashMap.putIfAbsent(i,"A"+i);
            }
        }
    }


    class WriteThreasTwo implements Runnable{
        public void run() {
            for(int i=1;i<10;i++){
                concurrentHashMap.putIfAbsent(i,"B"+i);
            }
        }
    }

    class ReadThread implements Runnable{
        public void run() {
            Iterator<Integer> iterator = concurrentHashMap.keySet().iterator();
            while (iterator.hasNext()){
                Integer key = iterator.next();
                System.out.println(key + " : " + concurrentHashMap.get(key));
            }
        }
    }

    public static void main(String[] args) {
        ExecutorService service = Executors.newFixedThreadPool(3);
        ConcurrentHashMapDemo ob = new ConcurrentHashMapDemo();
        service.execute(ob.new WriteThreasOne());
        service.execute(ob.new WriteThreasTwo());
        service.execute(ob.new ReadThread());

        service.shutdownNow();
    }
}
