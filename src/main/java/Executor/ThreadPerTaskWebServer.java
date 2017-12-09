package Executor;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by fang on 2017/12/9.
 * 创建多个线程.
 */
public class ThreadPerTaskWebServer {
    public static void main(String[] args) throws Exception {
        ServerSocket socket = new ServerSocket(80);
        while (true){
            final Socket connection = socket.accept();
            Runnable task = new Runnable() {
                public void run() {
                    handleRequest(connection);
                }


            };
            new Thread().start();

        }
    }

    private static void handleRequest(Socket connection) {
        //...
    }

}
