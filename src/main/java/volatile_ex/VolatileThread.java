package volatile_ex;

/**
 * Created by fang on 2017/11/20.
 */
public class VolatileThread implements Runnable{

    public int a;
    public int b;
    public volatile int c;


    public static void main(String[] args) {
        VolatileThread volatileThread = new VolatileThread();
        Thread thread = new Thread(volatileThread);
        thread.start();

        System.out.println(volatileThread.display(volatileThread.a));
        System.out.println(volatileThread.display(volatileThread.b));
        System.out.println(volatileThread.display(volatileThread.c));

    }

    public int display(int value){
        return value;
    }

    public void run() {
        this.a = 1;
        this.b = 2;
        this.c = 3;
    }


}
