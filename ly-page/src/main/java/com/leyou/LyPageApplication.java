package com.leyou;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author Nie ZongXin
 * @date 2019/9/15 21:00
 */
@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class LyPageApplication {
    public static void main(String[] args) {
        SpringApplication.run(LyPageApplication.class,args);

    }
}
