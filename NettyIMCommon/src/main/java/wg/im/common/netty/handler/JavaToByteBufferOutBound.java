package wg.im.common.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

@Slf4j
public class JavaToByteBufferOutBound extends  ChannelOutboundHandlerAdapter {


    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ByteBuf outb = ctx.alloc().ioBuffer();
        String outString=(String)msg;
        outb.writeCharSequence(outString, Charset.defaultCharset());
        super.write(ctx, outb, promise);
        log.info("outhandlerd被调用");

    }

    @Override
    public void read(ChannelHandlerContext ctx) throws Exception {
        log.info("read 被调用");

        ctx.read();
    }
}
