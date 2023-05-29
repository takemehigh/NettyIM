package wg.pojo;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import wg.im.common.bean.proto.msg.ProtoMsg;

/**
 * @author gw
 * @version 1.0
 * @description: TODO
 * @date 2023/4/12 2:41 下午
 */
@Data
public class MessagePO {

    private String content;

    public void fillMsg(ProtoMsg.MessageRequest.Builder cb) {
        if(StringUtils.isNotBlank(content)){
            cb.setContent(content);
        }
    }
}
