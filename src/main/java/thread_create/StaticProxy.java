package thread_create;

/**
 * Created by fang on 2017/11/30.
 * 静态代理模式. 动态代理是动态创建的.
 * 1 真实角色
 * 2 代理角色
 * 3 两者实现相同的接口.
 * 4 代理角色要持有真实角色的引用.
 */
public class StaticProxy {
    public static void main(String[] args) {
        //创建真实角色.
        You you = new You();
        //创建代理角色.
        WeddingCompany weddingCompany = new WeddingCompany(you);
        weddingCompany.marry();
    }
}

interface Marry{
    public abstract void marry();
}

//真是的角色
class You implements Marry{

    public void marry() {
        System.out.println("你和武松结婚了......");
    }
}

//代理角色.
class WeddingCompany implements Marry{
    private Marry you;
    public WeddingCompany(){
    }
    public WeddingCompany(You myyou){
        this.you = myyou;
    }

    public void before(){
        System.out.println("=====准备婚礼=====");
    }

    public void after(){
        System.out.println("===婚礼结束后收拾======");
    }

    public void marry() {
        before();
        you.marry();
        after();
    }

}
