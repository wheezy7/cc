import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.LockSupport;

@Slf4j(topic = "c.test6")
public class test6 {
    static Thread t1;
    static Thread t2;
    static Thread t3;

    public static void main(String[] args) {

        park p=new park(5);
        t1=new Thread(()->{
            p.print("q",t2);
        }
        );
        t2=new Thread(()->{
            p.print("w",t3);
        }
        );
        t3=new Thread(()->{
            p.print("e",t1);
        }
        );
        t1.start();
        t2.start();
        t3.start();
        LockSupport.unpark(t1);

    }

}

 class park{
    private int loop;

     public park(int loop) {
         this.loop = loop;
     }
    public void print(String str,Thread next){
        for(int i=0;i<loop;i++){
            LockSupport.park();
            System.out.print(str);
            LockSupport.unpark(next);

        }

    }
 }