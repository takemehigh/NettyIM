package wg.chatClient;


import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


/**
* @description: TODO
* @author gw
* @date 2023/2/7 2:57 下午
* @version 1.0
*/
@Slf4j
@Component
public class ChatClient {

    private final int port;
    private final int serverPort;
    Bootstrap bootstrap = new Bootstrap();
    EventLoopGroup eventLoopGroup;
    private boolean connectFlag = false;

    @Autowired
    public ChatClient(int port,int serverPort){
        System.out.println("netty客户端对象创建");
        this.port = port;
        this.serverPort = serverPort;
        this.eventLoopGroup = new NioEventLoopGroup(1);

    }

    //用来监听客户端尝试连接的结果
    ChannelFutureListener connectedListener = new ChannelFutureListener() {
        @Override
        public void operationComplete(ChannelFuture future) throws Exception {
            //看当前客户端是否连上了，判断依据是future的成功与否
            final EventLoop eventLoop = future.channel().eventLoop();
            if(future.isSuccess()){
                log.info("连接客户端成功!");
                connectFlag = true;
                notifyWaiter();
            }
        }
    };

    private void notifyWaiter() {

        this.notify();

    }

    public void startClient(){
        System.out.println("netty端口为"+port);

        //把连接任务交给线程池来做
        doStartConncet();

    }

    private void doStartConncet() {
        ChannelFuture channelFuture = null;
        System.out.println("netty端口为"+port);
        bootstrap.group(eventLoopGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.localAddress(port);
        bootstrap.remoteAddress("127.0.0.1",serverPort);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {



            }
        });
        try{
            channelFuture = bootstrap.connect();
            channelFuture.addListener(connectedListener);
        }
        catch (Exception e){
            log.error("连接IM服务器失败，异常信息",e);
        }

        //ChannelFuture channelFuture = bootstrap.bind().sync();
        log.info(" 客户端链接成功");
        // 7 等待通道关闭的异步任务结束
        // 服务监听通道会一直等待通道关闭的异步任务结束
        ChannelFuture closeFuture =
                channelFuture.channel().closeFuture();
        closeFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                log.info("通道关闭");
            }
        });

    }


}
