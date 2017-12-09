package Executor;

/**
 * Created by fang on 2017/12/9.
 */
public class ThreadSizeConfig {
    public static void main(String[] args) {
        int N_CPU = Runtime.getRuntime().availableProcessors();

        System.out.println(N_CPU);
    }
}
