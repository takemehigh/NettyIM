package wg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;
import wg.chatserver.ChatServer;

/**
 * @author gw
 * @version 1.0
 * @description: TODO
 * @date 2023/2/21 2:35 下午
 */
@SpringBootApplication
@ImportResource("classpath:applicationContext.xml")
public class NettyServerApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(NettyServerApplication.class);
        ChatServer ds = (ChatServer) applicationContext.getBean("chatServer");
        ds.runServer();
    }
}
