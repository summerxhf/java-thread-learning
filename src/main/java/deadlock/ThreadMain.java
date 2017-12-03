package deadlock;

/**
 * Created by fang on 2017/12/3.
 */
public class ThreadMain {
    public static void main(String[] args) {
        //共享的资源.
        Movie m  = new Movie();

        //多线程.
        Player player = new Player(m);
        Watcher watcher = new Watcher(m);

        new Thread(player).start();
        new Thread(watcher).start();
    }


}
