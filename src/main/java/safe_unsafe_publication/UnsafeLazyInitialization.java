package safe_unsafe_publication;
/**
 * Created by fang on 2017/11/24.
 * 不安全发布,一个线程正在初始化,另一个线程可能读到未构造完全的对象.
 */
public class UnsafeLazyInitialization {
    private static Resource resource;

    public static Resource getInstance(){
        if(resource == null){
            resource = new Resource();
        }
        return resource;
    }
}
