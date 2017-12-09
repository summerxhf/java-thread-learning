package Executor;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by fang on 2017/12/9.
 */
public class SingleThreadWebServer {
    public static void main(String[] args) throws Exception {
        ServerSocket socket = new ServerSocket(80);
        while (true){
            Socket connection = socket.accept();
            handleRequest(connection);
        }
    }

    private static void handleRequest(Socket connection) {
        //...
    }
}
