package com.leyou.auth.task;

import com.leyou.auth.config.JwtProperties;
import com.leyou.auth.service.AuthService;
import com.leyou.auth.service.impl.AuthServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Nie ZongXin
 * @date 2019/9/20 16:35
 */
@Slf4j
@Component
public class PrivilegeTokenHolder {
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private AuthServiceImpl authService;
    private String token;
//    private static final Long TOKEN_REFRESH_INTERVAL = 86400000L;
//    private static final Long TOKEN_RETRY_INTERVAL=10000L;

    public String getToken() {
        return token;
    }

    @Scheduled(fixedDelay = 86400000L)
    public void loadToken() throws InterruptedException {
        while (true) {
            try {

                this.token = authService.authoriz(jwtProperties.getApp().getId(), jwtProperties.getApp().getSecret());
                log.info("授权微服务获取token成功");
                break;
            } catch (Exception e) {
               log.error("授权微服务获取token失败",e);
            }
            Thread.sleep(10000L);
        }
    }
}
