package wg.sender;

import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wg.controller.CommandController;
import wg.converter.ChatConverter;
import wg.im.common.bean.proto.msg.ProtoMsg;
import wg.im.common.concurrent.FutureTaskScheduler;
import wg.pojo.MessagePO;

/**
 * @author gw
 * @version 1.0
 * @description: TODO
 * @date 2023/4/12 2:13 下午
 */
@Component
@Slf4j
public class ChatSender extends Sender{

    @Autowired
    CommandController commandController;

    public ChannelFuture sendChatMessage(String content) {
        MessagePO messagePO = new MessagePO();
        messagePO.setContent(content);
        ProtoMsg.Message msg =
                ChatConverter.build(messagePO);
        return doSend(msg);

    }

    @Override
    protected void sendfailed(ProtoMsg.Message message, ChannelFuture future) {
        log.error("发送消息失败");


        //重新创建连接
        //FutureTaskScheduler.add(()->commandController.startConnect());

    }

    @Override
    protected void sendSucced(ProtoMsg.Message message, ChannelFuture future) {
        log.info("发送消息成功");
    }
}
