package wg.converter;

import lombok.Data;
import wg.im.common.bean.proto.msg.ProtoMsg;
import wg.pojo.MessagePO;

/**
 * @author gw
 * @version 1.0
 * @description: TODO
 * @date 2023/4/12 3:19 下午
 */
@Data
public class ChatConverter extends BaseConverter {

    private MessagePO messagePO;

    public ChatConverter() {
        super(ProtoMsg.HeadType.MESSAGE_REQUEST);
    }

    public ProtoMsg.Message build0(MessagePO messagePO) {

        this.messagePO = messagePO;

        ProtoMsg.Message.Builder outerBuilder = getOuterBuilder();

        ProtoMsg.MessageRequest.Builder cb =
                ProtoMsg.MessageRequest.newBuilder();
        //填充字段
        this.messagePO.fillMsg(cb);
        ProtoMsg.Message requestMsg = outerBuilder.setMessageRequest(cb).build();
        return requestMsg;
    }

    public static ProtoMsg.Message build(MessagePO messagePO) {
        ChatConverter cc = new ChatConverter();
        return cc.build0(messagePO);
    }
}
