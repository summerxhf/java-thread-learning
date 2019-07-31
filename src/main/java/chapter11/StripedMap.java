package chapter11;


/**
 * Created by IntelliJ IDEA.
 * User: XINGHAIFANG
 * Date: 2019/7/31
 * Time: 10:51
 * 基于散列的map中使用锁分段技术
 */
public class StripedMap {
    private static final int N_LOCKS = 16;
    private final Node[] buckets;

    private final Object[] locks;

    private static class Node{
        Node next;
        Object key;
        Object value;
    }

    private final int hash(Object key){
        return Math.abs(key.hashCode() % buckets.length);
    }

   public Object get(Object key){
        int hash = hash(key);
        synchronized (locks[hash % N_LOCKS]){
            for(Node m =buckets[hash];m!=null; m =m.next ){
                if(m.key.equals(key)){
                    return m.value;
                }
            }
        }
        return null;
   }

   public void clear(){
        for(int i = 0;i<buckets.length;i++){
            synchronized (locks[i% N_LOCKS]){
                buckets[i] = null;
            }
        }
   }
}
