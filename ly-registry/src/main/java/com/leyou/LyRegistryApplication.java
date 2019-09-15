package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author Nie ZongXin
 * @date 2019/9/6 10:31
 */
@SpringBootApplication
@EnableEurekaServer
public class LyRegistryApplication {

    public static void main(String[] args) {
        SpringApplication.run(LyRegistryApplication.class,args);
    }
}
