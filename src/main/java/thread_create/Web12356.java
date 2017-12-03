package thread_create;

import javax.swing.plaf.synth.SynthRadioButtonMenuItemUI;

/**
 * Created by fang on 2017/11/30.
 *
 */
public class Web12356 implements Runnable{
    private int num=50;
    public void run(){
        try{
            int j = 2/0;
        }catch (Exception e){
            System.out.println("我是异常.");
            e.printStackTrace();
        }
        while (true){
            if(num<0){
                break;
            }
//            try {
//                //Thread.sleep(1000);//run()方法不能对外声明异常的.
//                //加了延时数据可能就不准确了.
//                //加上sleep可能就不安全了.
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            System.out.println(Thread.currentThread().getName() + "抢到了----" + num--);
        }
    }

    public static void main(String[] args) {
        //真实角色.
        Web12356 web12356 = new Web12356();
        Thread thread1 = new Thread(web12356,"路人甲");
        Thread thread2 = new Thread(web12356,"路人乙");
        Thread thread3 = new Thread(web12356,"路人丙");
        //启动线程.
        thread1.start();
        thread2.start();
        thread3.start();
    }

}
