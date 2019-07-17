package chapter6;

import thread_create.callable.Call;

import java.util.concurrent.*;

/**
 * Created by IntelliJ IDEA.
 * User: XINGHAIFANG
 * Date: 2019/7/17
 * Time: 15:57
 * 为任务设定时限;
 */
public class RenderWithTimeBudget {
    private static final Ad DEFAULT_AD = new Ad();
    private static final long TIME_BUDGET = 1000;
    private static final ExecutorService exec = Executors.newCachedThreadPool();

    Page renderPageWithAd() throws InterruptedException {
        long endTime = System.nanoTime()+ TIME_BUDGET;
        Future<Ad> f = exec.submit(new FetchAdTask());
        Page page = renderPageBody();
        Ad ad;
        try{
            long  timeLeft = endTime - System.nanoTime();
            //根据请求超时时间判断是否显示默认页面;
            ad = f.get(timeLeft, TimeUnit.NANOSECONDS);
        }catch (ExecutionException e){
            ad = DEFAULT_AD;
        }catch (TimeoutException e){
            ad = DEFAULT_AD;
            f.cancel(true);
        }

        return page;
    }


    Page renderPageBody(){
        return new Page();
    }
    static class Ad {
    }
    static class Page {
        public void setAd(Ad ad) { }
    }
    static class FetchAdTask implements Callable<Ad>{
        @Override
        public Ad call() {
            return new Ad();
        }
    }

}





