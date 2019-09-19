package com.leyou.sms.config;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author Nie ZongXin
 * @date 2019/9/17 20:11
 */
@Configuration
@EnableConfigurationProperties(SmsProperties.class)
public class SmsConfiguration {
    @Bean
    public IAcsClient getDefaultProfile(SmsProperties properties){
         DefaultProfile profile = DefaultProfile.getProfile(properties.getRegionID(),
                properties.getAccessKeyID(),
                properties.getAccessKeySecret());
         return new DefaultAcsClient(profile);
    }
}
