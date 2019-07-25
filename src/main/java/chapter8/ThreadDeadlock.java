package chapter8;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by IntelliJ IDEA.
 * User: XINGHAIFANG
 * Date: 2019/7/25
 * Time: 15:32
 * 线程饥饿死锁
 */
public class ThreadDeadlock {
    ExecutorService exec = Executors.newSingleThreadExecutor();

    public class LoadFileTask implements Callable<String>{
        private final String fileName;
        public LoadFileTask(String fileName){
            this.fileName = fileName;
        }

        public String call() throws Exception{
            return "";
        }
    }

    //线程启动;
    public class RenderPageTask implements Callable<String>{
        @Override
        public String call() throws Exception{
            Future<String> header,footer;
            header = exec.submit(new LoadFileTask("header.html"));
            footer = exec.submit(new LoadFileTask("footer.html"));
            String page = renderBody();
            return header.get()+page+footer.get();
        }

        private String renderBody(){
            return "";
        }
    }
}
