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
public class NettyServerConfig {

    NettyServerConfig(){
        System.out.println("NettyServerConfig创建");
    }

    @Value("${netty.port}")
    int port;

    @Bean(name="port")
    public int getPort(){
        return port;
    }

}
