package com.leyou.search.config;


import com.leyou.search.task.PrivilegeTokenHolder;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Nie ZongXin
 * @date 2019/9/20 19:06
 */
@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor requestInterceptor(JwtProperties jwtProperties, PrivilegeTokenHolder privilegeTokenHolder) {
        return new PrivilegeInterceptor(jwtProperties, privilegeTokenHolder);
    }

    private class PrivilegeInterceptor implements RequestInterceptor {
        private JwtProperties jwtProperties;
        private PrivilegeTokenHolder privilegeTokenHolder;

        public PrivilegeInterceptor(JwtProperties jwtProperties, PrivilegeTokenHolder privilegeTokenHolder) {
            this.jwtProperties = jwtProperties;
            this.privilegeTokenHolder = privilegeTokenHolder;
        }
//请求头中放入token
        @Override
        public void apply(RequestTemplate requestTemplate) {
            requestTemplate.header(jwtProperties.getApp().getHeaderName(), privilegeTokenHolder.getToken());
        }
    }
}
