package chapter7;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * Created by IntelliJ IDEA.
 * User: XINGHAIFANG
 * Date: 2019/7/18
 * Time: 11:17
 * 通过改写interrupt方法将非标准的取消操作封装在Thread中
 */
public class ReaderThread extends Thread{
    private final Socket socket;
    private final InputStream in;
    private static final int BUFSZ = 512;

    public ReaderThread(Socket socket) throws  IOException {
        this.socket = socket;
        this.in = socket.getInputStream();
    }


    //封装中断方法;
    public void interrupt(){
        try{
            socket.close();
        }catch (IOException ignored){}
        finally{
            super.interrupt();
        }
    }


    public void run(){
        try{
            byte[] buf = new byte[BUFSZ];
            while (true){
                int count = in.read(buf);
                if(count<0){
                    break;
                }else if(count>0){
                   processBuffer(buf,count);
                }
            }
        }catch (IOException e){
            //允许线程退出;
        }
    }

    public void processBuffer(byte[] buf, int count) {
    }
}
