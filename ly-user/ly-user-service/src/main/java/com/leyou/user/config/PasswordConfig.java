package com.leyou.user.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.SecureRandom;

/**
 * @author Nie ZongXin
 * @date 2019/9/18 16:35
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "ly.encoder.crypt")
public class PasswordConfig {

    private String secret;
    private int strength;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
//        利用密钥生成随机安全码
        SecureRandom secureRandom = new SecureRandom(secret.getBytes());
//        初始化BCryptPasswordEncoder
        return new BCryptPasswordEncoder(strength, secureRandom);
    }


}
