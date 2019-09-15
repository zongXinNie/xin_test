package com.leyou.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Nie ZongXin
 * @date 2019/9/9 11:04
 */
@Configuration
public class OSSConfig {


    @Bean
    public OSS ossClient(OSSProperties ossProperties){
        return new OSSClientBuilder().build(ossProperties.getEndpoint(),
                ossProperties.getAccessKeyId(),ossProperties.getAccessKeySecret());
    }
}
