package chapter4;

import net.jcip.annotations.NotThreadSafe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: XINGHAIFANG
 * Date: 2019/7/12
 * Time: 16:51
 * 使用了不同的锁, 分别用于list和ListHelper,锁的不是同一个对象;
 */
@NotThreadSafe
public class ListHelper<E> {
    public List<E> list = Collections.synchronizedList(new ArrayList<E>());
    public synchronized boolean putIfAbsent(E x){
        boolean absent = !list.contains(x);
        if(absent){
            list.add(x);
        }
        return absent;
    }


//    public static void main(String[] args) {
//        ListHelper<NumberRange> listHelper = new ListHelper<NumberRange>();
//        NumberRange numberRange = new NumberRange();
//        System.out.println(listHelper.putIfAbsent(numberRange));
//    }
}
