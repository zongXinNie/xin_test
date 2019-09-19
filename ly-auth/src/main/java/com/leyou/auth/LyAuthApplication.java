package com.leyou.auth;

import com.leyou.auth.config.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author Nie ZongXin
 * @date 2019/9/19 12:26
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.leyou.user")
@EnableConfigurationProperties(JwtProperties.class)
public class LyAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(LyAuthApplication.class,args);
    }
}
