package wg.converter;

import lombok.Data;
import wg.im.common.bean.proto.msg.ProtoMsg;
import wg.pojo.MessagePO;

/**
 * @author gw
 * @version 1.0
 * @description: TODO
 * @date 2023/4/12 3:02 下午
 */
@Data
public class BaseConverter {

    protected ProtoMsg.HeadType headType;

    public BaseConverter() {
    }

    public BaseConverter(ProtoMsg.HeadType headType) {
        this.headType = headType;
    }

    protected ProtoMsg.Message.Builder getOuterBuilder() {
        ProtoMsg.Message.Builder mb = ProtoMsg.Message.newBuilder()
                .setType(headType);
        return mb;
    }
}
