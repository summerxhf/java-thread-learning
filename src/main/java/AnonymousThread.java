import java.util.concurrent.*;

/**
 * Created by IntelliJ IDEA.
 * User: XINGHAIFANG
 * Date: 2019/7/17
 * 匿名创建线程的几种方式
 */
public class AnonymousThread {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //方式一:使用匿名内部类创建线程的子类对象;
        Thread thread = new Thread() {
            public void run() {
                System.out.println("使用匿名内部类创建线程的子类对象");
            }
        };

        thread.start();


        //方式二:使用匿名内部类创建线程的子类匿名对象;
        new Thread() {
            @Override
            public void run() {
                System.out.println("使用匿名内部类创建线程的就子类匿名对象");
            }
        }.start();


    //方式三:使用匿名内部类的方式,创建线程执行目标类对象
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            System.out.println("使用匿名内部类方式,创建线程执行目标类对象");
        }
    };

    Thread thread2 = new Thread(runnable);
    thread2.start();

    //方式四:使用匿名内部类方式,创建线程执行目标匿名对象;
        Thread thread3 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("使用匿名内部类方式,创建线程执行目标匿名对象");
            }
        });

    //方式五:使用匿名内部类方式,创建线程的匿名对象
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("使用匿名内部类方式,创建线程的匿名对象");
            }
        }).start();

        //方式六: lambda形式使用线程
        new Thread(()->{
            System.out.println("lambda形式使用线程");
        }).start();


        //方式七:待返回值形式使用Callable  futuretask获取带有返回值的任务;
        Callable<Integer> call = ()->{
            System.out.println("带有返回值的线程执行");
            return 11;
        };

        //将任务封装成FutureTask;
        FutureTask<Integer> futureTask = new FutureTask<>(call);

        //开启线程,执行线程任务;
        new Thread(futureTask).start();
        System.out.println("输入带有返回值的结果: " +futureTask.get());

        //方式八: 使用ExecutorService Callable Future实现带有返回结果的多线程
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        Callable<Integer> callable = new MyCallable("吃饭任务");
        Future future = executorService.submit(callable);
        System.out.println("ExecutorService Callable Future返回结果-------------"+future.get());

        //关闭线程池;
        executorService.shutdown();
    }

//    CopyOnWriteArrayList<Integer> copyOnWriteArrayList = new CopyOnWriteArrayList<>();


 }


 class MyCallable implements Callable<Integer>{
    private String taskName;

    public MyCallable(String taskName){
        this.taskName = taskName;
    }

     @Override
     public Integer call() {
         System.out.println("任务执行中----------------------");
         try {
             Thread.sleep(1000);
         } catch (InterruptedException e) {
             e.printStackTrace();
         }
         System.out.println("任务执行完毕-----------------------");
         return 2222;
     }
 }
