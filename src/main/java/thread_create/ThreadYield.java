package thread_create;

/**
 * Created by fang on 2017/12/3.
 * 暂停自己,让出cpu调度.yield
 */
public class ThreadYield   extends Thread{
    //yild 暂停线程,是一个静态方法.
    public static void main(String[] args) throws InterruptedException {
        ThreadYield threadYield = new ThreadYield();
        Thread thread = new Thread(threadYield);//xinzheng
        thread.start();//就绪状态.
        //cpu调度进入运行.


        for(int i=0;i<100;i++){
            if(i%20==0){
                //暂停本线程.
                //写在谁的线程里面就暂停谁.;
                Thread.yield();//是一个静态方法.写在main中暂停main()
            }
            System.out.println("main....." + i);
        }
    }


    public void run(){
        for(int i=0;i<100;i++){
            System.out.println("yield...." +i);
        }
    }


}
