package netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;

import java.nio.ByteBuffer;

import static io.netty.buffer.ByteBufUtil.appendPrettyHexDump;
import static io.netty.util.internal.StringUtil.NEWLINE;

public class testslice {
    public static void main(String[] args) {
        /* 将大的bytebuff变为小的bytebuff*/
/*        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(9);
        buffer.writeBytes(new byte[]{'1','2','3','4','5','6','7','8','9'});
        log(buffer);

        ByteBuf slice = buffer.slice(0, 5);
        slice.retain();
        ByteBuf slice1 = buffer.slice(5, 4);

        buffer.release();
        log(slice);
        log(slice);
        log(slice1);
        System.out.println("+++++++++++++++++++++++++++++++=");
        slice.setByte(0,'q');
        log(slice);
        log(buffer);*/


    /*将小的bytebuff变为大的bytebuff*/
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        buffer.writeBytes(new byte[]{1,2,3,4,5});

        ByteBuf buffer2 = ByteBufAllocator.DEFAULT.buffer();
        buffer.writeBytes(new byte[]{1,2,3,4,5});
        CompositeByteBuf byteBufs = ByteBufAllocator.DEFAULT.compositeBuffer();
        byteBufs.addComponents(true,buffer,buffer2);
        log(byteBufs);


    }
    private static void log(ByteBuf buffer) {
        int length = buffer.readableBytes();
        int rows = length / 16 + (length % 15 == 0 ? 0 : 1) + 4;
        StringBuilder buf = new StringBuilder(rows * 80 * 2)
                .append("read index:").append(buffer.readerIndex())
                .append(" write index:").append(buffer.writerIndex())
                .append(" capacity:").append(buffer.capacity())
                .append(NEWLINE);
        appendPrettyHexDump(buf, buffer);
        System.out.println(buf.toString());
    }
}
