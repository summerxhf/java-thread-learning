package thread_create;

/**
 * Created by fang on 2017/11/30.
 */
public  class Tortoise extends Thread{
    @Override
    public void run(){
        //线程体.
        for(int i=0;i<100;i++){
            System.out.println("乌龟跑了"+i +"步");
        }
    }
}
