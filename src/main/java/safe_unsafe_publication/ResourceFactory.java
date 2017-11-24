package safe_unsafe_publication;

/**
 * Created by fang on 2017/11/24.
 *  延长初始化占位符.
 */
public class ResourceFactory {
    public static class ResourceHolder{
        public static Resource resource = new Resource();
    }

    public static Resource getResource(){
        return ResourceHolder.resource;
    }
}
