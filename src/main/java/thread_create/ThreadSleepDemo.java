package thread_create;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by fang on 2017/12/3.
 * sleep demo,暂停当前线程,并不会释放锁.
 * 倒数10个数,1s打印一个.
 */
public class ThreadSleepDemo {
    public static void main(String[] args) throws InterruptedException {


        //倒计时. 每一秒打印一次.
        Date endTime = new Date(System.currentTimeMillis() + 10*1000);//当前时间加上10s

        long end = endTime.getTime();
        while (true){
            System.out.println(new SimpleDateFormat("mm:ss").format(endTime));
            endTime = new Date(endTime.getTime()-1000);//减去1s
            Thread.sleep(1000);//等待一秒
            if(end-10*1000>endTime.getTime()){
                break;
            }
        }



        //倒数10个数,打印.
        int num=10;
        while (true){
            System.out.println(num--);
            Thread.sleep(1000);//暂停.

            if(num<=0){
                break;
            }
        }
    }
}
