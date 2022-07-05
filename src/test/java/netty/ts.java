package netty;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import static netty.BufferUtils.debugAll;
@Slf4j(topic = "c.ts")
public class ts {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel sever= ServerSocketChannel.open();
        Thread.currentThread().setName("Boss");
        sever.bind(new InetSocketAddress(8081));
        Selector boss= Selector.open();
        sever.configureBlocking(false);

        sever.register(boss, SelectionKey.OP_ACCEPT);
        // 创建固定数量的Worker
        // 创建固定数量的Worker
        worker[] workers = new worker[2];
        // 用于负载均衡的原子整数
        AtomicInteger robin = new AtomicInteger(0);
        for(int i = 0; i < workers.length; i++) {
            workers[i] = new worker("worker-"+i);
        }
        while (true){
            boss.select();
            Set<SelectionKey> selectionKeys = boss.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
           /* boss负责连接事件*/
            while (iterator.hasNext()){
                SelectionKey next = iterator.next();
                iterator.remove();
                if(next.isAcceptable()){
                    SocketChannel accept = sever.accept();
                    log.debug("connected....{}",accept.getRemoteAddress());
                    accept.configureBlocking(false);
                    log.debug("before register....{}",accept.getRemoteAddress());
                  workers[robin.getAndIncrement()% workers.length].register(accept);
                    log.debug("after register....{}",accept.getRemoteAddress());


                }
            }
        }

    }

     static class worker implements Runnable{
        private Thread thread;
        private volatile Selector selector;
        private String name;
         private volatile boolean start=false;
         private ConcurrentLinkedDeque<Runnable> queue;


         public worker(String name) {
             this.name = name;
         }
         public  void register(final SocketChannel sc) throws IOException {
            /* 只启动一次*/
            if (start==false){
                thread=new Thread(this,name);
                selector = selector.open();
                queue= new ConcurrentLinkedDeque<>();
                thread.start();
                start=true;
            }
            queue.add(()->{
                try {
                    sc.register(selector,SelectionKey.OP_READ);
                } catch (ClosedChannelException e) {
                    e.printStackTrace();
                }
            });
            selector.wakeup();
         }
         @Override
        public void run() {
             while (true){
                 try {
                     selector.select();/*就为了阻塞用*/
                     Runnable poll = queue.poll();
                     if(poll != null){
                         poll.run();
                     }
                     Set<SelectionKey> selectionKeys = selector.selectedKeys();
                     Iterator<SelectionKey> iterator = selectionKeys.iterator();
                     while (iterator.hasNext()){
                         SelectionKey next = iterator.next();
                         iterator.remove();
                         if (next.isReadable()){
                             try {
                                 SocketChannel channel = (SocketChannel) next.channel();
                                 ByteBuffer buffer = ByteBuffer.allocate(16);
                                 log.debug("reading....{}",channel.getRemoteAddress());
                                 int read = channel.read(buffer);
                                 if(read==-1){
                                     next.cancel();
                                 }else {
                                     buffer.flip();
                                     debugAll(buffer);
                                 }
                             } catch (IOException e) {
                                 next.cancel();
                                 e.printStackTrace();
                             }

                         }
                     }
                 } catch (IOException e) {

                     e.printStackTrace();
                 }
             }

        }
    }
}
