package netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
@Slf4j(topic = "c.myclient")
public class mycilent {
    public static void main(String[] args) throws IOException, InterruptedException {
        ChannelFuture channel = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(
                        new ChannelInitializer<NioSocketChannel>() {
                            @Override
                            protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                               nioSocketChannel.pipeline().addLast(new StringEncoder());
                            }

                })
                .connect(new InetSocketAddress("localhost", 8081));
        channel.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {

                Channel channel1=channelFuture.channel();
               log.debug("{}",channel1);
               channel1.writeAndFlush("hello world")       ;
            }
        });



    }
}