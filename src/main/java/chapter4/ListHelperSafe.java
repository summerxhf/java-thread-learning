package chapter4;

import net.jcip.annotations.NotThreadSafe;
import net.jcip.annotations.ThreadSafe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: XINGHAIFANG
 * Date: 2019/7/12
 * Time: 16:51
 * 若没有则添加
 * 锁定的全局变量list, 对list是同一把锁;
 */
@ThreadSafe
public class ListHelperSafe<E> {
    public List<E> list = Collections.synchronizedList(new ArrayList<E>());
    public  boolean putIfAbsent(E x){
        synchronized (list){
            boolean absent = !list.contains(x);
            if(absent){
                list.add(x);
            }
            return absent;
        }
    }

}
