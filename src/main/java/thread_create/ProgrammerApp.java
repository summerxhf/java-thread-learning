package thread_create;

/**
 * Created by fang on 2017/11/30.
 *  1 创建真实角色.
 *  2 创建代理角色.
 *  3 调用.start()启动线程.
 */
public class ProgrammerApp {
    public static void main(String[] args) {
        Programmer programmer = new Programmer();
        Thread proxy= new Thread(programmer);
        proxy.start();

        for(int i=0;i<1000;i++){
            System.out.println("一边来去聊qq..");
        }

    }

}
