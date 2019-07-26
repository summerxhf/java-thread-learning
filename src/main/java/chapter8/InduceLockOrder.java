package chapter8;

/**
 * Created by IntelliJ IDEA.
 * User: XINGHAIFANG
 * Date: 2019/7/26
 * Time: 16:29
 * 通过锁顺序来避免死锁
 */
public class InduceLockOrder {
    private static final Object tieLock = new Object();

    public void transferMondy(final Acctount fromAcct,final Acctount toAcct,final DollarAmount amount)
    throws InsufficientFundsException{
        class Helper{
            public void transfer() throws InsufficientFundsException{
                if(fromAcct.getBalance().compareTo(amount)<0){
                    throw new InsufficientFundsException();

                }else {
                    fromAcct.debit(amount);
                    toAcct.debit(amount);
                }
            }
        }

        int fromHash = System.identityHashCode(fromAcct);
        int toHash = System.identityHashCode(toAcct);
        if(fromHash<toHash){
            synchronized (fromAcct){
                synchronized (toAcct){
                    new Helper().transfer();
                }
            }
        }else if(fromHash>toHash){
            synchronized (toAcct){
                synchronized (fromAcct){
                    new Helper().transfer();
                }
            }
        }else {
            synchronized (tieLock){
                synchronized (fromAcct){
                    synchronized (toAcct){
                        new Helper().transfer();
                    }
                }
            }
        }

    }


    interface DollarAmount extends Comparable<DollarAmount>{}

    interface Acctount{
        void debit(DollarAmount dollarAmount);
        void credit(DollarAmount dollarAmount);
        DollarAmount getBalance();
        int getAcctNo();
    }

    class InsufficientFundsException extends Exception{
    }
}
