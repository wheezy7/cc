import lombok.extern.slf4j.Slf4j;

@Slf4j(topic="c.test1")
public class test1 {
    public static void main(String[] args) throws InterruptedException {
        twop twop = new twop();
        twop.start();
        Thread.sleep(3200);
        twop.stop();
    }
}
@Slf4j(topic="c.twop")
class twop{
    private Thread monitor;
    public void start(){
        monitor=new Thread(()->{
            while (true){
                Thread thread = Thread.currentThread();
                if(thread.isInterrupted()){
                    log.debug("料理后事");
                    break;
                }
                try {
                    Thread.sleep(1000);
                    log.debug("执行打印记录");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    thread.interrupt();
                }
            }
        });

        monitor.start();
    }
    public void stop(){
        monitor.interrupt();
    }

}
