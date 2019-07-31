package chapter11;

import net.jcip.annotations.GuardedBy;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: XINGHAIFANG
 * Date: 2019/7/31
 * Time: 9:45
 * @GuardedBy( lock )来标识。里面的Lock是告诉维护者：这个状态变量，这个方法被哪个锁保护着。这样可以强烈的提示类的维护者注意这里。
 * 减小锁的粒度,对锁进行分解;
 */
public class ServerStatus {
    @GuardedBy("users") public final Set<String> users;
    @GuardedBy("queries") public final Set<String> queries;

    public ServerStatus() {
        users = new HashSet<String>();
        queries = new HashSet<String>();
    }

    public synchronized void addUser(String u){
        users.add(u);
    }

    public synchronized void addQuery(String q){
        queries.add(q);
    }

    public synchronized void removeUser(String u){
        users.remove(u);
    }

    public synchronized void removeQuery(String q){
        queries.remove(q);
    }

    //将上面的方式改为使用锁分解技术;
    public void addUserNew(String u){
        synchronized (users){

            users.add(u);
        }
    }

    public void addQueryNew(String q){
        synchronized (queries){
            queries.add(q);
        }
    }

    public void removeUserNew(String u){
        synchronized (users){
            users.remove(u);
        }
    }

    public void removeQueryNew(String q){
        synchronized (queries){
            queries.remove(q);
        }
    }


}
