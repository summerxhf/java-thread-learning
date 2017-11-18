package unsafeThread;

/**
 * Created by fang on 2017/11/18.
 */
public class SafeThreadClient {
    public static void main(String[] args) {

        for(int i=0;i<10000;i++){
            UnsafeSequence unsafeSequence = new UnsafeSequence();
            Thread thread = new Thread(unsafeSequence);
            thread.start();
        }

    }
}
