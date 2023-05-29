package wg.im.common.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;
import wg.im.common.bean.proto.msg.ProtoMsg;
import wg.im.common.constant.ProtoConstant;

/**
 * @author gw
 * @version 1.0
 * @description: TODO
 * @date 2023/4/12 4:22 下午
 */
@Slf4j
public class MessageEncoder extends MessageToByteEncoder<ProtoMsg.Message> {
    @Override
    protected void encode(ChannelHandlerContext ctx, ProtoMsg.Message msg, ByteBuf out) throws Exception {
        //out是新申请的
        //详细头用于确保信息符合规范

        out.writeShort(ProtoConstant.MAGIC_NUM);
        out.writeShort(ProtoConstant.VERSION);

        byte[] msgArr = msg.toByteArray();

        log.info("出站编码器，数据二进制长度为"+msgArr.length);
        out.writeInt(msgArr.length);

        out.writeBytes(msgArr);

    }
}
