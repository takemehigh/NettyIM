package wg.session;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.AttributeKey;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import wg.im.common.pojo.User;

import java.util.UUID;

/**
 * @author gw
 * @version 1.0
 * @description: TODO
 * @date 2023/4/13 3:00 下午
 */
@Data
@Slf4j
public class ClientSession {

    public static final AttributeKey<ClientSession> SESSION_KEY =   AttributeKey.valueOf("SESSION_KEY");


    /**
     * 用户实现客户端会话管理的核心
     */
    private Channel channel;
    //private User user;

    /**
     * 保存登录后的服务端sessionid
     */
    private String sessionId;

    private boolean isConnected = false;
    private boolean isLogin = false;

    private User user;


    //绑定通道
    //连接成功之后
    public ClientSession(Channel channel) {
        //正向的绑定
        this.channel = channel;
        this.sessionId = UUID.randomUUID().toString();
        //反向的绑定
        channel.attr(SESSION_KEY).set(this);
    }

    public void close(){
        isConnected = false;
        isLogin = false;

        ChannelFuture future = channel.close();
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    log.error("连接顺利断开");
                }
            }
        });
    }
}
