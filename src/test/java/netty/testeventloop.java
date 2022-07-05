package netty;


import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.NettyRuntime;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class testeventloop {
    public static void main(String[] args) {
        /*创建事件循环组*/
        EventLoopGroup group=new NioEventLoopGroup(4) ;
        System.out.println(NettyRuntime.availableProcessors());
        /*获取事件循环对象*/
        System.out.println(group.next());
       /* 执行普通任务*/
     /*  group.next().submit(()->{
                   try {
                       Thread.sleep(1000);
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
                   log.debug("ok");

               }

       );*/
   /*  执行定时任务*/
     group.next().scheduleAtFixedRate(()->{
         log.debug("ok");
     },0,1,TimeUnit.SECONDS);
       log.debug("main");
    }

}
