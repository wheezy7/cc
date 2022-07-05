package message;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LoggingHandler;

public class test {

    public static void main(String[] args) throws Exception {
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(
                //防止粘包半包
                new LengthFieldBasedFrameDecoder(1024, 12,4,0,0),
                new LoggingHandler(),
                new massage()
        );
        loginmessage loginmessage = new loginmessage("zhangsan", "123456", "张三");
        embeddedChannel.writeOutbound(loginmessage);

        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        new massage().encode(null,loginmessage,buffer);

        embeddedChannel.writeInbound(buffer);
    }

}
