package netty;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

import static netty.BufferUtils.debugAll;
@Slf4j(topic = "c.server")
public class server {
    public static void main(String[] args) throws IOException {
        ByteBuffer allocate = ByteBuffer.allocate(16);
       /* 创建服务器*/
        ServerSocketChannel open = ServerSocketChannel.open();
        /*绑定端口*/
        open.bind(new InetSocketAddress(8081));
      /*  连接集合*/
        List<SocketChannel> channels=new ArrayList<>();
        while(true){
            log.debug("connecting...");
            SocketChannel accept = open.accept();
            log.debug("after connecting....{}",accept);
            channels.add(accept);
            for(SocketChannel channel : channels){
                log.debug("before read.....{}",channel);
                channel.read(allocate);
                allocate.flip();
                debugAll(allocate);
                allocate.clear();
                log.debug("after read.....{}",channel);
            }

        }

    }
}
