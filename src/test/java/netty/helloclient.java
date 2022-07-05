package netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

public class helloclient {
    public static void main(String[] args) throws InterruptedException {
        new Bootstrap()
                .group(new NioEventLoopGroup())
                // 选择客户 Socket 实现类，NioSocketChannel 表示基于 NIO 的客户端实现
                .channel(NioSocketChannel.class)
                // ChannelInitializer 处理器（仅执行一次）
                // 它的作用是待客户端SocketChannel建立连接后，执行initChannel以便添加更多的处理器
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        // 消息会经过通道 handler 处理，这里是将 String => ByteBuf 编码发出
                       channel.pipeline().addLast(new ChannelInboundHandlerAdapter(){

                           /*会在建立连接后进行*/
                           @Override
                           public void channelActive(ChannelHandlerContext channelHandlerContext) throws Exception {

                               ByteBuf buffer = channelHandlerContext.alloc().buffer();
                               buffer.writeBytes("hello".getBytes());
                               channelHandlerContext.writeAndFlush(buffer);
                           }
                           @Override
                           public void channelRead(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
                               ByteBuf s =(ByteBuf)o;
                               System.out.println(s.toString(Charset.defaultCharset()));

                           }
                       });
                    }
                })
                // 指定要连接的服务器和端口
                .connect(new InetSocketAddress("localhost", 8081));
                // Netty 中很多方法都是异步的，如 connect
                // 这时需要使用 sync 方法等待 connect 建立连接完毕
             /*   .sync();*/
                // 获取 channel 对象，它即为通道抽象，可以进行数据读写操作
               /* .channel()
                // 写入消息并清空缓冲区
                .writeAndFlush("hello world");*/
    }
}