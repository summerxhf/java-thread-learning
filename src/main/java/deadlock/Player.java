package deadlock;

/**
 * Created by fang on 2017/12/3.
 * 生产者.
 */
public class Player implements Runnable{
    private Movie m;
    public Player(Movie m){
        this.m = m;
    }
    public void run() {
        for(int i=0;i<20;i++){
            if(0==i%2){
                try {
                    m.play("左青龙");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else {
                try {
                    m.play("右白虎");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
