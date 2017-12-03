package thread_create;

/**
 * Created by fang on 2017/12/3.
 *合并线程,join
 */
public class ThreadJoinDemo extends Thread {



    public static void main(String[] args) throws InterruptedException {
        ThreadJoinDemo threadJoinDemo = new ThreadJoinDemo();
        Thread thread = new Thread(threadJoinDemo);//xinzheng
        thread.start();//就绪状态.
        //cpu调度进入运行.


        for(int i=0;i<100;i++){
            if(i==50){
                thread.join();//前50是join和main执行顺序不定,当i==50加上join,则thread先执行完,main再执行.
                //main阻塞
            }
            System.out.println("main....." + i);
        }
    }

    public void run(){
        for(int i=0;i<100;i++){
            System.out.println("join...." +i);
        }
    }
}
