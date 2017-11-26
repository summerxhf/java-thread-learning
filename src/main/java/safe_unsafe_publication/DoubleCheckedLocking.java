package safe_unsafe_publication;

/**
 * Created by fang on 2017/11/25.
 * 双重检查锁初始化DCL
 */
public class DoubleCheckedLocking {

    private static volatile Resource resource;

    public static Resource getInstance(){
        if(resource == null){
            synchronized (DoubleCheckedLocking.class){
                if(resource == null){
                    resource = new Resource();
                }
            }
        }
        return resource;
    }
}
