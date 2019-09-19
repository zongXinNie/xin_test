package com.leyou.sms.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * @author Nie ZongXin
 * @date 2019/9/17 19:58
 */

@Data
@ConfigurationProperties(prefix = "ly.sms")
public class SmsProperties {
    private String accessKeyID;
    private String accessKeySecret;
    /**
     * 短信签名
     */
    private String signName;
    /**
     * 短信模板
     */
    private String verifyCodeTemplate;
    /**
     * 发送短信请求的域名
     */
    private String domain;
    /**
     * API版本
     */
    private String version;
    /**
     * API类型
     */
    private String action;
    /**
     * 区域
     */
    private String regionID;

}
