package wg.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author gw
 * @version 1.0
 * @description: TODO
 * @date 2023/2/21 3:11 下午
 */
@Configuration
public class NettyClientConfig {

    NettyClientConfig(){
        System.out.println("NettyServerConfig创建");
    }

    @Value("${netty.port}")
    int port;

    @Value("${netty.ip}")
    String ip;

    @Value("${netty.server.port}")
    int serverPort;

    @Bean(name="ip")
    public String getIp(){return ip;}

    @Bean(name="port")
    public int getPort(){
        return port;
    }

    @Bean(name="serverPort")
    public int getServerPortPort(){
        return serverPort;
    }

}
