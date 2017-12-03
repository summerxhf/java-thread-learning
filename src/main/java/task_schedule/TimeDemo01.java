package task_schedule;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by fang on 2017/12/3.
 * 了解任务调度,java自带的任务调度,封装了线程等.
 * 扩展spring的任务调度
 */
public class TimeDemo01 {
    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("so easy 线程体.");
            }
        },new Date(System.currentTimeMillis()+1000),200);//每隔200ms运行一次.

    }
}
