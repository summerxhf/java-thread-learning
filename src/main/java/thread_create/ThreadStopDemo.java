package thread_create;

/**
 * Created by fang on 2017/12/3.
 * 终止线程方式.
 * 1自然
 * 2手动
 */
public class ThreadStopDemo {
    public static void main(String[] args) {
        Study study =  new Study();
        new Thread(study).start();

        //外部干涉
        for(int i=0;i<100;i++){
            if(i==50){//外部干涉
                study.stop();
            }
            System.out.println("main..." +i);
        }
    }
}

class Study implements Runnable{
    //定义标识.
    private boolean flag = true;
    //线程体中使用标识.
    public void run() {
        while (flag){
            System.out.println("Study 线程体..");
        }


    }

    //(3)对外提供改变标识.
    public void stop(){
        this.flag = false;
    }
}
