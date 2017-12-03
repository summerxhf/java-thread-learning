package deadlock;

/**
 * Created by fang on 2017/12/3.
 *  生产者消费者模式信号灯法
 *  wait()会释放锁,sleep()不会释放锁.抱着锁睡觉.
 *  唤醒 notify() 或者notifyAll()
 */
public class Movie{

    private String pic;//共同的资源.
    //信号灯.ture时生产者生产,消费者等待. 生产完成后通知消费者.
    //flag false 消费者消费,生产者等待. 消费完成后通知生产.
    //用到等待和通知.
    private boolean flag = true;

    public synchronized void play(String pic) throws InterruptedException {
        if(!flag){//生产者等待.
            this.wait();
        }
        //开始生产.耗时500ms.
        Thread.sleep(500);
        // 生产完毕.
        System.out.println("生产了...."+ pic);
        this.pic = pic;

        //通知消费.
        this.notify();
        //生产者停下.
        this.flag = false;
    }

    public synchronized  void watch() throws InterruptedException {
        if(flag){
            this.wait();//消费者等待.
        }
        //开始消费.
        Thread.sleep(200);
        //消费完毕.
        System.out.println("消费了----"+pic);

        //通知生产.
        this.notify();

        //消费停止.
        this.flag = true;
    }

}
