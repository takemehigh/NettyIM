package wg.im.common.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

@Slf4j
public class OutBoundHandlerDemoE extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        String s = (String)msg;
        s=s+"二二";
        ctx.writeAndFlush(s);
        //再重复来
//        for (int a = 1; a < 5; a++) {
//            ctx.writeAndFlush(s);
//        }
    }

    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {

        //测试writeType和SETtype方法
        ByteBuf bb = (ByteBuf)msg;
        bb.writeCharSequence(new String("二二"), Charset.defaultCharset());
        int i = 0;
        byte[] b = new byte[bb.readableBytes()];
        bb.getBytes(0,b);
        String inString = new String(b,Charset.defaultCharset());
        bb.release();
        ctx.writeAndFlush(inString);
        //再重复来
        for (int a = 1; a < 5; a++) {
            ctx.writeAndFlush(inString);
        }

    }
}
