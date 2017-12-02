package thread_create.callable;


import java.util.concurrent.*;

/**
 * Created by fang on 2017/11/30.
 * 使用Callable创建线程.
 */
public class Call {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        //创建线程.
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Race tortoise = new Race("千年乌龟",1000);
        Race rabbit = new Race("兔子",500);
        Future<Integer> result1 = executorService.submit(tortoise);
        Future<Integer> result2 = executorService.submit(rabbit);


        Thread.sleep(2000);//2秒.
        rabbit.setFlag(false);//停止线程体循环.
        tortoise.setFlag(false);

        int num1 = result1.get();
        int num2 = result2.get();

        System.out.println("乌龟跑了---->" + num1);
        System.out.println("兔子跑了--->" + num2);

        //停止这个线程服务.
        executorService.shutdown();
    }
}

class Race implements Callable<Integer>{
    private String name;//名称
    private long time;//延时时间.
    private boolean flag = true;
    private int step = 0; //走了多少步骤.

    public Race(){

    }
    public Race(String name){//构造方法.
        super();
        this.name = name;
    }
    public Race(String name,long time){//名称和设置sleep时间.
        super();
        this.name = name;
        this.time = time;
    }


    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws Exception if unable to compute a result
     */
    public Integer call() throws Exception {
        while (flag){
            Thread.sleep(time);//延时
            step++;
        }
        return step;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

}
