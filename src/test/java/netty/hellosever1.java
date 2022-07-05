package netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

@Slf4j
public class hellosever1 {
    public static void main(String[] args) {
      /*  如果一个handler处理时间过长，就在建立一个group去处理*/
        EventLoopGroup group = new DefaultEventLoopGroup();
        // 1、启动器，负责装配netty组件，启动服务器
        new ServerBootstrap()
                // 2、创建 NioEventLoopGroup，可以简单理解为 线程池 + Selector
                .group(new NioEventLoopGroup(),new NioEventLoopGroup(2))
                // 3、选择服务器的 ServerSocketChannel 实现
                .channel(NioServerSocketChannel.class)
                // 4、child 负责处理读写，该方法决定了 child 执行哪些操作
                // ChannelInitializer 处理器（仅执行一次）
                // 它的作用是待客户端SocketChannel建立连接后，执行initChannel以便添加更多的处理器
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        // 5、SocketChannel的处理器，使用StringDecoder解码，ByteBuf=>String
                        // 6、SocketChannel的业务处理，使用上一个处理器的处理结果
                       nioSocketChannel.pipeline().addLast("handle1",new ChannelInboundHandlerAdapter(){

                           @Override
                           public void channelRead(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
                             ByteBuf s =(ByteBuf)msg;
                               System.out.println(s.toString(Charset.defaultCharset()));
                               ByteBuf buffer = channelHandlerContext.alloc().buffer();
                               buffer.writeBytes(s);
                               channelHandlerContext.writeAndFlush(buffer);

                           }
                       });
                    }
                    // 7、ServerSocketChannel绑定8080端口
                })
                .bind(8081);
    }
}