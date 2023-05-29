package wg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;
import wg.chatClient.ChatClient;
import wg.controller.CommandController;

/**
 * @author gw
 * @version 1.0
 * @description: TODO
 * @date 2023/2/21 2:35 下午
 */
@SpringBootApplication
//@ImportResource("classpath:applicationContext.xml")
public class NettyClientApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(NettyClientApplication.class);
        CommandController commandController = (CommandController) applicationContext.getBean("commandController");
        commandController.startRunningThread();
    }
}
