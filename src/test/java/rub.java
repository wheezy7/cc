import lombok.extern.slf4j.Slf4j;

@Slf4j(topic="c.rub")
public class rub {
    public static void main(String[] args) {
 /*       Thread t=new Thread(){
            @Override
            public void run(){
                log.debug("running");
            }
        };
        t.setName("t1");
        t.start();
        log.debug("running2");*/

        Runnable runnable = () -> log.debug("running");
        Thread t=new Thread(runnable,"t2");
        t.start();
    }
}
