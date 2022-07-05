import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j(topic = "c.test7")
public class test7 {
  static awi aw=new awi(5);
  static Condition a=aw.newCondition();
    static Condition b=aw.newCondition();
    static Condition c=aw.newCondition();
    public static void main(String[] args) {
        new Thread(()->{
            aw.print("q",a,b);
        },"t1").start();
        new Thread(()->{
            aw.print("w", b,c);
        },"t2"
        ).start();
        new Thread(()->{
            aw.print("e", c,a);
        },"t3"
        ).start();
        try {
            Thread.sleep(1000);
            aw.lock();
            a.signal();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            aw.unlock();
        }
    }

}

class awi extends ReentrantLock{
    private int loop;

    public awi(int loop) {
        this.loop = loop;
    }
    public void print(String str,Condition xian, Condition next)  {
        for (int i=0;i<loop;i++){
            lock();
            try {
                xian.await();
                System.out.print(str);
                next.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                unlock();
            }
        }
    }
}