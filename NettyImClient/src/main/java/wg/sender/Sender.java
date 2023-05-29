package wg.sender;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.Data;
import wg.im.common.bean.proto.msg.ProtoMsg;
import wg.session.ClientSession;

/**
 * @author gw
 * @version 1.0
 * @description: TODO
 * @date 2023/4/12 2:06 下午
 */
@Data
public abstract class Sender {


    private ClientSession c;

    
    public ChannelFuture doSend(ProtoMsg.Message message){
        ChannelFuture f = c.getChannel().writeAndFlush(message);
        f.addListener(new GenericFutureListener<ChannelFuture>() {
            @Override
            public void operationComplete(ChannelFuture future)
                    throws Exception {
                // 回调
                if (future.isSuccess()) {
                    sendSucced(message,future);
                } else {
                    sendfailed(message,future);
                }
            }

        });
        return f;
    }

    protected abstract void sendfailed(ProtoMsg.Message message, ChannelFuture future);

    protected abstract void sendSucced(ProtoMsg.Message message,ChannelFuture future);

}
