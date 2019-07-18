package chapter7;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 * User: XINGHAIFANG
 * Date: 2019/7/18
 * Time: 10:53
 * 在专门的线程中中断任务;
 */
public class TimeRun2 {
    private static final ScheduledExecutorService cancelExec = new ScheduledThreadPoolExecutor(1);

    public static void timeRun(final Runnable runnable, long timeout, TimeUnit unit) throws InterruptedException{
        class RethrowableTask implements Runnable{
            private volatile Throwable t;

            @Override
            public void run() {
                try {
                    runnable.run();
                }catch (Throwable t){
                    this.t = t;
                }
            }

            void rethrow(){
                if(t!=null){

                }
            }


        }

        RethrowableTask task = new RethrowableTask();
        final Thread taskThread = new Thread(task);
        taskThread.start();
        cancelExec.schedule(new Runnable() {
            @Override
            public void run() {
                taskThread.interrupt();
            }
        },timeout,unit);
        taskThread.join(unit.toMillis(timeout));
        task.rethrow();
    }


}
