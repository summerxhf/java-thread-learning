package chapter8;

import javax.xml.bind.Element;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * Created by IntelliJ IDEA.
 * User: XINGHAIFANG
 * Date: 2019/7/26
 * Time: 10:55
 * 将串行改为并行;
 */
public class TransformingSequential {
    //并行执行;
    void processSequentially(List<Element> elements){
        for(Element e:elements){
//            process(e);
        }
    }
    //串行执行;
    void processInParallel(Executor exec,List<Element> elements){
        for(final Element e:elements){
            exec.execute(new Runnable() {
                @Override
                public void run() {
//                    process(e);
                }
            });
        }
    }
}
