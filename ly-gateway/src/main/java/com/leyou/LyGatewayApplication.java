package com.leyou;

import com.leyou.config.FilterProperties;
import com.leyou.config.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Nie ZongXin
 * @date 2019/9/6 10:38
 */
@SpringCloudApplication
@EnableZuulProxy  //开启zuul
@EnableFeignClients
@EnableScheduling  //开启定时任务
@EnableConfigurationProperties({JwtProperties.class, FilterProperties.class})
public class LyGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(LyGatewayApplication.class,args);
    }
}
