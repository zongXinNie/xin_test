package com.leyou.search.config;

import com.leyou.utils.RsaUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.PublicKey;

/**
 * @author Nie ZongXin
 * @date 2019/9/20 23:41
 */
@Slf4j
@Data
@ConfigurationProperties("ly.jwt")
public class JwtProperties implements InitializingBean {
    private String pubKeyPath;
    private PublicKey publicKey;
    private AppTokenProperties app = new AppTokenProperties();

    @Data
    public class AppTokenProperties {
        private Long id;
        private String secret;
        private String headerName;
    }
    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        } catch (Exception e) {
            log.error("商品服务获取公钥失败");
            throw new RuntimeException(e);
        }
    }
}
