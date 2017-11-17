package unsafeThread;

/**
 * test for thread
 * Created by fang on 2017/11/17.
 */
public class UnsafeThreadClient {
    public static void main(String[] args) {
        UnsafeSequence unsafeSequence = new UnsafeSequence();
        for(int i=0;i<10000;i++){
            Thread thread = new Thread(unsafeSequence);
            thread.start();
        }

    }
}
