package wg.chatserver;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import wg.im.common.netty.handler.*;

/**
* @description: TODO
* @author gw
* @date 2023/2/7 2:57 下午
* @version 1.0
*/
@Slf4j
@Component
public class ChatServer {

    private final int port;
    ServerBootstrap serverBootstrap = new ServerBootstrap();

    //@Autowired
    public ChatServer(@Qualifier(value = "port") int port){
        System.out.println("netty对象创建");
        this.port = port;
        //this.runServer();
    }

    public void runServer(){
        System.out.println("netty端口为"+port);
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        //构造方法不传入数量取CPU核心数二倍的线程数
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try{
            serverBootstrap.group(bossGroup,workerGroup);
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.localAddress(port);
            serverBootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {

                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new MessageDecoder());
                    //ch.pipeline().addLast(new MessageEncoder());

                    //   ch.pipeline().addLast(new DiscardInBoundHandler());
//                    ch.pipeline().addLast("echo",new NettyEchoHandler());
//
//                    ch.pipeline().addLast(new JavaToByteBufferOutBound());
//
//                    ch.pipeline().addLast(new OutBoundHandlerDemoE());

                }

            });
            ChannelFuture channelFuture = serverBootstrap.bind().sync();
            log.info(" 服务器启动成功，监听端口: " +
                    channelFuture.channel().localAddress());
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
            log.info(" 同步等待关闭");
                    closeFuture.sync();
        }
        catch (Exception e){
            e.printStackTrace();
        } finally {
            // 8 优雅关闭 EventLoopGroup
            // 释放掉所有资源包括创建的线程
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }


    }

//    public static void main(String[] args) {
//        System.out.println(Runtime.getRuntime().availableProcessors());
//        DiscardServer discardServer = new DiscardServer(8099);
//        discardServer.runServer();
//    }
}
