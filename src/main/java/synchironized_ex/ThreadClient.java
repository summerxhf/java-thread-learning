package synchironized_ex;

/**
 * Created by fang on 2017/11/23.
 * main方法
 */
public class ThreadClient {
    public static void main(String[] args) {
        for(int i = 0;i<3; i++){
            Thread myThread = new InstanceThread();
            myThread.start();
        }
    }
}
