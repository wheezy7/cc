import lombok.extern.slf4j.Slf4j;

import static java.lang.Thread.sleep;

@Slf4j(topic="c.test")
public class test {
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(()->{

            while(true){
                boolean interrupted = Thread.currentThread().isInterrupted();
                if(interrupted){
                    System.out.println("成功打断");
                    break;
                }
            }
        }, "t1");
        t1.start();
        Thread.sleep( 1000);
        log.debug("interuppte");
        t1.interrupt();

    }

}
