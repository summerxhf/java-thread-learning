package safe_unsafe_publication;


/**
 * Created by fang on 2017/11/24.
 * 安全的延迟加载
 */
public class SafeLazyInitialization {
    private static Resource resource;

    public synchronized static Resource getInstance(){
        if(resource ==null){
            resource = new Resource();
        }
        return resource;
    }
}
