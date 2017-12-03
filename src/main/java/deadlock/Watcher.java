package deadlock;

/**
 * Created by fang on 2017/12/3.
 * 消费者.
 */
public class Watcher implements Runnable{
    private Movie m;
    public Watcher(Movie m){
        this.m = m;
    }

    public void run() {
        for(int i=0;i<20;i++){
            try {
                m.watch();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
