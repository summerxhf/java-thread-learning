package safe_unsafe_publication;

/**
 * Created by fang on 2017/11/24.
 * 提前初始化方式.
 */
public class EagerInitialzation {
    private static Resource resource = new Resource();

    public static Resource getResource(){
        return resource;
    }
}
