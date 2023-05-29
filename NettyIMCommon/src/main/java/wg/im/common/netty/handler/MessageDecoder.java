package wg.im.common.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;
import wg.im.common.bean.proto.msg.ProtoMsg;
import wg.im.common.constant.ProtoConstant;

import java.util.List;

/**
 * @author gw
 * @version 1.0
 * @description: TODO
 * @date 2023/4/12 4:46 下午
 */
@Slf4j
public class MessageDecoder extends ByteToMessageDecoder {


    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        Object obj=decode0(ctx,in);
        if(obj!=null){
            out.add(obj);
        }
    }

    Object decode0(ChannelHandlerContext ctx, ByteBuf in) throws Exception {

        in.markReaderIndex();
        //读魔数
        if(in.readableBytes()<8){
            log.error("消息投长度不够8位");
            return null;
        }
        //
        if(in.readShort()!= ProtoConstant.MAGIC_NUM){
            throw new Exception("魔数不对");
        }
        if(in.readShort()!= ProtoConstant.VERSION){
            throw new Exception("版本不对");
        }

        int length = in.readInt();
        log.info("要读的消息长度为"+length);

        //如果可读字节数小于长度，那说明消息没完整，需要重置读下标
        if(length>in.readableBytes()){
            in.resetReaderIndex();
            throw new Exception("数据不完整");
        }
        //判断bytebuf是什么类型的
        byte[] dst;
//        if(in.hasArray()){
//            //hasArray说明是堆内存,直接赋值
//            dst = in.readBytes()
//        }
//        else{
//
//        }
        dst = new byte[length];
        in.readBytes(dst, 0, length);
        ProtoMsg.Message m = ProtoMsg.Message.parseFrom(dst);

        return m;
    }
}
