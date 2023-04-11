package wg.im.common.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.util.List;

//@ChannelHandler.Sharable
@Slf4j
public class NettyEchoHandler extends ReplayingDecoder<String> {


    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int strlength=in.readInt();

        String cs = (String)in.readCharSequence(strlength,Charset.defaultCharset());

        log.info("msg type: " + (in.hasArray()?"堆内存":"直接内存"));
        log.info("bytebuf最大容量："+in.maxCapacity());


        log.info("server received: " + cs);

        //写回数据，异步任务
        //ByteBuf byteBuf=((ByteBuf) msg);
        //byteBuf.read
        //log.info("写回前，msg.refCnt:" + byteBuf.refCnt());

        out.add(cs);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        log.info("通道注册完成:");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        log.info("通道激活完成:");

    }
}
