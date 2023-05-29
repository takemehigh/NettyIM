package wg.chatClient;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wg.im.common.netty.handler.MessageDecoder;
import wg.im.common.netty.handler.MessageEncoder;


/**
* @description: 负责和服务端创建连接
* @author gw
* @date 2023/2/7 2:57 下午
* @version 1.0
*/
@Slf4j
@Component
@Data
public class ChatClient {

    //private final int port;
    private final int serverPort;
    private final String ip;

    Bootstrap bootstrap;
    EventLoopGroup eventLoopGroup;
    //private boolean connectFlag = false;

    @Autowired
    public ChatClient(String ip,int serverPort){
        System.out.println("netty客户端对象创建");
        this.ip = ip;
        this.serverPort = serverPort;
        this.eventLoopGroup = new NioEventLoopGroup(1);

    }

    //用来监听客户端尝试连接的结果
    ChannelFutureListener connectedListener;

    public void startClient(){
        doStartConncet();
    }




    private void doStartConncet() {
        ChannelFuture channelFuture = null;
        System.out.println("netty端口为"+serverPort);
        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup);
        bootstrap.channel(NioSocketChannel.class);
        //bootstrap.localAddress(port);
        bootstrap.remoteAddress(ip,serverPort);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {

                ch.pipeline().addLast(new MessageEncoder());
                //ch.pipeline().addLast(new MessageDecoder());

            }
        });
        try{
            channelFuture = bootstrap.connect();
            channelFuture.addListener(connectedListener);

        }
        catch (Exception e){
            log.error("连接IM服务器失败，异常信息",e);
        }

        // 7 等待通道关闭的异步任务结束
        // 服务监听通道会一直等待通道关闭的异步任务结束
//        ChannelFuture closeFuture =
//                channelFuture.channel().closeFuture();
//        closeFuture.addListener(new ChannelFutureListener() {
//            @Override
//            public void operationComplete(ChannelFuture future) throws Exception {
//                log.info("通道关闭");
//            }
//        });

    }


}
