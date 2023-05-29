package wg.controller;

import ch.qos.logback.core.net.server.Client;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoop;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import wg.chatClient.ChatClient;
import wg.command.ChatConsoleCommand;
import wg.command.Command;
import wg.command.CommandCollectCommand;
import wg.command.LoginConsoleCommand;
import wg.im.common.concurrent.FutureTaskScheduler;
import wg.im.common.pojo.User;
import wg.sender.ChatSender;
import wg.session.ClientSession;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * @author gw
 * @version 1.0
 * @description: 负责建立连接和收集各种用户输入
 * @date 2023/4/11 1:25 下午
 */
@Controller("commandController")
@Data
@Slf4j
public class CommandController {


    @Autowired
    private ChatClient chatClient;

    @Autowired
    private ChatConsoleCommand chatConsoleCommand;

    @Autowired
    private LoginConsoleCommand loginConsoleCommand;
    @Autowired
    private CommandCollectCommand commandCollectCommand;
    @Autowired
    ChatSender chatSender;
    private Channel c;

    private ClientSession cs;

    private boolean connectFlag = false;

    @PostConstruct
    public void initCommandCollect(){
        log.info("初始化命令收集command");
        Map<String, Command> commandMap= new HashMap<>();
        commandMap.put(loginConsoleCommand.getKey(),loginConsoleCommand);
        commandMap.put(chatConsoleCommand.getKey(),chatConsoleCommand);
        commandCollectCommand.initComand(commandMap);
    }


    ChannelFutureListener closedListener = new ChannelFutureListener() {
        @Override
        public void operationComplete(ChannelFuture future) throws Exception {

            log.info("连接已经断开");
            log.info(new Date() + ": 连接已经断开……");
            c = future.channel();

            // 创建会话
            ClientSession session =
                    c.attr(ClientSession.SESSION_KEY).get();
            session.close();
        }
    };

    ChannelFutureListener connectedListener = new ChannelFutureListener() {
        @Override
        public void operationComplete(ChannelFuture future) throws Exception {
            //看当前客户端是否连上了，判断依据是future的成功与否
            Channel channel=future.channel();
            final EventLoop eventLoop = future.channel().eventLoop();
            if(future.isSuccess()){
                log.info("连接客户端成功!需要登录！");
                c=channel;
                c.closeFuture().addListener(closedListener);
                cs = new ClientSession(c);
//                //创建一个用户让服务端储存用户id方便传消息
//                User user = new User();
//                user.setUserName(user.getUserName()+user.getUid());
//                cs.setUser(user);
//                cs.setConnected(true);

                connectFlag=true;

                //创建会话信息
                notifyWaiter();
            }
            else{
                log.info("连接客户端失败，1s后重试");
                connectFlag = false;
                eventLoop.schedule(()-> doConnect(),1,TimeUnit.SECONDS);
            }
        }
    };

    //休眠
    public synchronized void waitCommandThread() {

        //休眠，命令收集线程
        try {
            this.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private synchronized void notifyWaiter() {

        this.notifyAll();

    }

    public void doConnect(){
        FutureTaskScheduler.add(()->{
            //开始连接前先把连接监听器set给客户端用于处理连接成功or失败后的逻辑
            chatClient.setConnectedListener(connectedListener);
            //chatClient.setConnectedListener(closedListener);
            chatClient.startClient();
        });
//        chatClient.setConnectedListener(connectedListener);
//        chatClient.startClient();
    }

    public void startRunningThread(){



        while(true){
            try{
                while(!connectFlag){
                    doConnect();
                    waitCommandThread();
                }
                while(cs!=null){
                    Scanner scanner = new Scanner(System.in);
                    commandCollectCommand.exec(scanner);
                    String in = commandCollectCommand.getIn();

                    switch (in){
                        case LoginConsoleCommand.KEY:
                            loginConsoleCommand.exec(scanner);
                        case ChatConsoleCommand.KEY:
                            chatConsoleCommand.exec(scanner);
                    }

                }
            }
            catch (Exception e){
                log.error("发生异常",e);
            }


        }


    }

    //发送单聊消息
    private void sendMsg(ChatConsoleCommand c) {
        //登录
        if (!isConnectFlag()) {
            log.info("还没有登录，请先登录");
            return;
        }
    }

    void close(){

    }
}
