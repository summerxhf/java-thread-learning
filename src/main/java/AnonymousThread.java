import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: XINGHAIFANG
 * Date: 2019/7/17
 * 匿名创建线程的几种方式
 */
public class AnonymousThread {
    public static void main(String[] args) {
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
    }

//    CopyOnWriteArrayList<Integer> copyOnWriteArrayList = new CopyOnWriteArrayList<>();

}
