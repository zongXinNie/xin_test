package com.leyou.config;

import com.leyou.utils.CookieUtils;
import com.leyou.utils.JwtUtils;
import com.leyou.utils.RsaUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.PublicKey;

/**
 * @author Nie ZongXin
 * @date 2019/9/19 20:41
 */
@Slf4j
@Data
@ConfigurationProperties(prefix = "ly.jwt")
public class JwtProperties implements InitializingBean {
    private String pubKeyPath;
    private PublicKey publicKey;

    private UserTokenProperties user = new UserTokenProperties();
    private PrivilegeTokenProperties app = new PrivilegeTokenProperties();

    @Data
    public class PrivilegeTokenProperties {
        private Long id;
        private String secret;
        private String headerName;
    }

    @Data
    public class UserTokenProperties {
        private String cookieName;
        private String heradName;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        } catch (Exception e) {
            log.error("zuul读取公钥失败", e);
            throw new RuntimeException(e);
        }
    }
}
