package chapter8;

/**
 * Created by IntelliJ IDEA.
 * User: XINGHAIFANG
 * Date: 2019/7/26
 * Time: 16:04
 * 动态的锁顺序,死锁;
 * 一个线程从x向y转账;另一个线程从y向x转账;
 * 如果操作顺序不当,可能死锁
 */
public class DynamicLockDemo {
   public void transferMoney(Object fromAccount,Object toAccount,Object dollarAmount){
       synchronized (fromAccount){
           synchronized (toAccount){
//               if(fromAccount<0){
//                   throw new insufficientFundsException();
//               }else {
//                   fromAccount.debit(amount);
//                   toAccount.debit(amount);
//               }
           }
       }
   }
}
