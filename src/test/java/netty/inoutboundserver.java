package netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

@Slf4j
public class inoutboundserver {
    public static void main(String[] args) {
        /*  如果一个handler处理时间过长，就在建立一个group去处理*/
        EventLoopGroup group = new DefaultEventLoopGroup();
        // 1、启动器，负责装配netty组件，启动服务器
        new ServerBootstrap()
                               .group(new NioEventLoopGroup(), new NioEventLoopGroup(2))
                              .channel(NioServerSocketChannel.class)
                               .childHandler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
            /*    通过channel拿到pipline*/
                ChannelPipeline pipeline = nioSocketChannel.pipeline();
                // 5、SocketChannel的处理器，使用StringDecoder解码，ByteBuf=>String
            /*   pipeline.addLast(new StringDecoder());*/
                // 6、SocketChannel的业务处理，使用上一个处理器的处理结果
                pipeline.addLast("h1", new ChannelInboundHandlerAdapter() {
                    @Override
                    public void channelRead(ChannelHandlerContext channelHandlerContext, Object s) throws Exception {
                        log.debug("1");
                        ByteBuf b=(ByteBuf)s;
                        String s1 = b.toString(Charset.defaultCharset());
                        /* h1调用h2可从下面方法选其一*/
                        /* channelHandlerContext.fireChannelRead(s);*/
                        super.channelRead(channelHandlerContext,s1);
                    }
                });
                pipeline.addLast("h2", new ChannelInboundHandlerAdapter() {
                    @Override
                    public void channelRead(ChannelHandlerContext channelHandlerContext,Object s) throws Exception {
                        log.debug("2");
                        student student = new student(s.toString());
                        /* channelHandlerContext.fireChannelRead(s);*/
                       super.channelRead(channelHandlerContext,student);
                    }
                });
                pipeline.addLast("h4", new ChannelOutboundHandlerAdapter() {
                    @Override
                    public void write(ChannelHandlerContext channelHandlerContext, Object o, ChannelPromise channelPromise) throws Exception {
                        log.debug("h4");
                        super.write(channelHandlerContext,o,channelPromise);
                    }
                });
                pipeline.addLast("h3", new ChannelInboundHandlerAdapter() {
                    @Override
                    public void channelRead(ChannelHandlerContext channelHandlerContext, Object s) throws Exception {
                        log.debug("3,{},class:{}",s,s.getClass());
                        super.channelRead(channelHandlerContext,s);
                       channelHandlerContext.writeAndFlush(channelHandlerContext.alloc().buffer().writeBytes("server".getBytes()));
                    }
                });

                pipeline.addLast("h5", new ChannelOutboundHandlerAdapter() {
                    @Override
                    public void write(ChannelHandlerContext channelHandlerContext, Object o, ChannelPromise channelPromise) throws Exception {
                        log.debug("h5");
                        super.write(channelHandlerContext,o,channelPromise);
                    }
                });
                pipeline.addLast("h6", new ChannelOutboundHandlerAdapter() {
                    @Override
                    public void write(ChannelHandlerContext channelHandlerContext, Object o, ChannelPromise channelPromise) throws Exception {
                        log.debug("h6");
                        super.write(channelHandlerContext,o,channelPromise);
                    }
                });
            }
            // 7、ServerSocketChannel绑定8080端口
        })
          .bind(8081);
    }
}
@Data
@AllArgsConstructor
@NoArgsConstructor
class student{
    private String name;
}
