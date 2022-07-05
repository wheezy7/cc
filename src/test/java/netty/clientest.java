package netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.logging.Logger;

public class clientest {
    static final org.slf4j.Logger log = LoggerFactory.getLogger(clientest.class);
    public static void main(String[] args) {
        send();
    }

    private static StringBuilder makestring(char c,int len){
        StringBuilder stringBuilder = new StringBuilder(len + 2);
        for(int i=0;i<len;i++){
            stringBuilder.append(c);
        }
        stringBuilder.append("\n");
        return stringBuilder;
    }
    private static void send() {
        NioEventLoopGroup worker = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.group(worker);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                    ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                        @Override
                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                            ByteBuf buffer = ctx.alloc().buffer();
                            char c='0';
                            Random r=new Random();
                            for (int i = 0; i < 10; i++) {
                              StringBuilder sb=makestring(c,r.nextInt(256)+1);
                              c++;
                               buffer.writeBytes(sb.toString().getBytes());
                            }
                            ctx.writeAndFlush(buffer);

                        }
                    });
                }
            });
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8081).sync();
            channelFuture.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            log.error("client error", e);
        } finally {
            worker.shutdownGracefully();
        }
    }
}