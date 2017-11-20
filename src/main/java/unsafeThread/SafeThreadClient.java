package unsafeThread;

/**
 * Created by fang on 2017/11/18.
 */
public class SafeThreadClient {
    public static void main(String[] args) {
        //利用每次都new来确保线程安全,但是需求却没法做到.
        for(int i=0;i<10000;i++){
            UnsafeSequence unsafeSequence = new UnsafeSequence();
            Thread thread = new Thread(unsafeSequence);
            thread.start();
        }

        //利用AtomicLong java.util.corrurnent包中的,线程安全类计数器.
        SafeSequence safeSequence = new SafeSequence();
        for(int i=0;i<10000;i++){
            Thread thread = new Thread(safeSequence);
            thread.start();
        }

    }
}
