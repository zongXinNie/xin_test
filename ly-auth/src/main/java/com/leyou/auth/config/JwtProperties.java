package com.leyou.auth.config;

import com.leyou.exception.LyException;
import com.leyou.utils.RsaUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author Nie ZongXin
 * @date 2019/9/19 13:17
 */
@Data
@Slf4j
@ConfigurationProperties(prefix = "ly.jwt")
public class JwtProperties implements InitializingBean {
    private String pubKeyPath;
    private String priKeyPath;
    private PublicKey publicKey;
    private PrivateKey privateKey;
//用户token相关属性
    private UserTokenProperties user=new UserTokenProperties();


    @Data
    public class UserTokenProperties {
        private int expire;
        private int minRefreshInterval;
        private String cookieName;
        private String cookieDomian;

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
            this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        } catch (Exception e) {
            log.error("初始化公私钥匙失败！", e);
            throw new RuntimeException(e);
        }
    }
}
