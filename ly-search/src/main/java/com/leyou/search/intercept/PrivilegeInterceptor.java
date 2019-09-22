package com.leyou.search.intercept;

import com.leyou.entity.AppInfo;
import com.leyou.entity.Payload;
import com.leyou.search.config.JwtProperties;
import com.leyou.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Nie ZongXin
 * @date 2019/9/21 19:26
 */
@Slf4j
public class PrivilegeInterceptor implements HandlerInterceptor {

    private JwtProperties jwtProperties;

    public PrivilegeInterceptor(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //TODO 其他微服务获取token未完成，暂时不做判断全部放行

        //        获取请求头中的token

        try {
/*            String token = request.getHeader(jwtProperties.getApp().getHeaderName());
//        解析token
            Payload<AppInfo> payload = JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey(), AppInfo.class);
            AppInfo appInfo = payload.getUserInfo();
            List<Long> targetList = appInfo.getTargetList();

           if (targetList==null||!targetList.contains(jwtProperties.getApp().getId())){
                throw new RuntimeException("没有权限访问本服务");
            }
            log.info("服务{}正在请求资源:{}",appInfo.getServiceName(),request.getRequestURI());*/
            return true;
        } catch (RuntimeException e) {
            log.error("服务访问被拒绝,token认证失败!",e);

            return false;

        }


    }
}
