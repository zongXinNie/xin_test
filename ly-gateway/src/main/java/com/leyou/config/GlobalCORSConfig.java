package com.leyou.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author Nie ZongXin
 * @date 2019/9/7 19:48
 */
@Configuration
@ConfigurationProperties(prefix = "ly.cors")
public class GlobalCORSConfig {

    private String[] allowedOrigins;
    private Boolean allowedCredentials;
    private String[] allowedHeaders;
    private String[] allowedMethods;
    private Long maxAge;
    private String filterPath;

    @Bean
    public CorsFilter corsFilter() {
        //添加CORS配置信息
        CorsConfiguration configuration = new CorsConfiguration();
//        允许的域，不要写*，否则cookie就无法使用了
        for (String allowedOrigin : allowedOrigins) {
            configuration.addAllowedOrigin(allowedOrigin);

        }
//        是否发送cookie信息
        configuration.setAllowCredentials(allowedCredentials);
//        允许的请求方式
        for (String allowedMethod : allowedMethods) {
            configuration.addAllowedMethod(allowedMethod);
        }
//        允许的头信息
        for (String allowedHeader : allowedHeaders) {
            configuration.addAllowedHeader(allowedHeader);
        }

//        有效期
        configuration.setMaxAge(maxAge);
//        添加映射路径，拦截一切请求
        UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
        configurationSource.registerCorsConfiguration(filterPath, configuration);
//        返回新的CORSFilter
        return new CorsFilter(configurationSource);
    }

    public String[] getAllowedOrigins() {
        return allowedOrigins;
    }

    public void setAllowedOrigins(String[] allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }

    public Boolean getAllowedCredentials() {
        return allowedCredentials;
    }

    public void setAllowedCredentials(Boolean allowedCredentials) {
        this.allowedCredentials = allowedCredentials;
    }

    public String[] getAllowedHeaders() {
        return allowedHeaders;
    }

    public void setAllowedHeaders(String[] allowedHeaders) {
        this.allowedHeaders = allowedHeaders;
    }

    public String[] getAllowedMethods() {
        return allowedMethods;
    }

    public void setAllowedMethods(String[] allowedMethods) {
        this.allowedMethods = allowedMethods;
    }

    public Long getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(Long maxAge) {
        this.maxAge = maxAge;
    }

    public String getFilterPath() {
        return filterPath;
    }

    public void setFilterPath(String filterPath) {
        this.filterPath = filterPath;
    }
}
