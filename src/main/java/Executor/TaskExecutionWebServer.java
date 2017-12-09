package Executor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by fang on 2017/12/9.
 * 固定长度的线程池.
 */
public class TaskExecutionWebServer {
    private static final int NTHREADS = 100;

    private static final Executor exec  = Executors.newFixedThreadPool(NTHREADS);

    public static void main(String[] args) throws IOException{
        ServerSocket serverSocket = new ServerSocket();
        while (true){
            final Socket connection = serverSocket.accept();
            Runnable task = new Runnable() {
                public void run() {
                    handleRequest(connection);
                }


            };

            exec.execute(task);
        }
    }

    private static void handleRequest(Socket connection) {
    }
}
