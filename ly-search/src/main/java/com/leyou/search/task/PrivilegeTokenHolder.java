package com.leyou.search.task;

import com.leyou.search.client.AuthClient;
import com.leyou.search.config.JwtProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Nie ZongXin
 * @date 2019/9/20 23:41
 */
@Slf4j
@Component
public class PrivilegeTokenHolder {
    @Autowired
    private AuthClient authClient;
    @Autowired
    private JwtProperties jwtProperties;
    private String token;

    public String getToken() {
        return token;
    }

    @Scheduled(fixedDelay = 86400000L)
    public void loadToken() throws InterruptedException {
        while (true) {
            try {
                this.token = authClient.authoriz(jwtProperties.getApp().getId(), jwtProperties.getApp().getSecret());
                log.info("搜索服务获取token成功");
                break;
            } catch (Exception e) {
                log.error("搜索服务获取token失败");
            }
            Thread.sleep(10000L);
        }


    }

}
