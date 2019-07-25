package chapter7;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by IntelliJ IDEA.
 * User: XINGHAIFANG
 * Date: 2019/7/24
 * Time: 11:18
 */
public class LogWriter {
    private final BlockingQueue<String> queue;
    private final LoggerThread logger;
    private static final int CAPACITY = 1000;
    public LogWriter(Writer writer) {
        this.queue = new LinkedBlockingQueue<String>(CAPACITY);
        this.logger = new LoggerThread(writer);
    }
    public void log(String msg) throws InterruptedException{
        queue.put(msg);
    }

    private class LoggerThread extends Thread{
        private final PrintWriter writer;
        public LoggerThread(Writer writer) {
            this.writer = new PrintWriter(writer, true); // autoflush
        }

        @Override
        public void run() {
            try{
                while (true){
                    writer.println(queue.take());
                }
            }catch (InterruptedException ignored){

            }finally {
                writer.close();
            }
        }
    }
}
