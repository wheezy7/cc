import lombok.extern.slf4j.Slf4j;

import java.util.Random;


@Slf4j(topic = "c.test3")
public class test3 {
    static final Object o= new Object();
    public static void main(String[] args) throws InterruptedException {
       new Thread(()->{
           synchronized (o){
               log.debug("huodesuo");
               try {
                   o.wait(6000);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }

           }
       },"t1").start();
       Thread.sleep(20);
       synchronized (o){
           System.out.println("laiba");
       }
    }
}
