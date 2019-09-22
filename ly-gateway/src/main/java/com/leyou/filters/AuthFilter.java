package com.leyou.filters;

import com.leyou.common.ExceptionEnum;
import com.leyou.config.FilterProperties;
import com.leyou.config.JwtProperties;

import com.leyou.entity.Payload;
import com.leyou.entity.UserInfo;
import com.leyou.exception.LyException;
import com.leyou.utils.CookieUtils;
import com.leyou.utils.JsonUtils;
import com.leyou.utils.JwtUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;


/**
 * @author Nie ZongXin
 * @date 2019/9/19 20:40
 */
@Slf4j
@Component
public class AuthFilter extends ZuulFilter {
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private FilterProperties filterProperties;

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.FORM_BODY_WRAPPER_FILTER_ORDER+1;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();
        String requestURI = request.getRequestURI();
//        判断白名单
        return !isAllowPath(requestURI);
    }

    private Boolean isAllowPath(String requestURI){
        boolean flag=false;
//        判断前缀
        for (String allowPath : filterProperties.getAllowPaths()) {
            if (requestURI.startsWith(allowPath)){
                flag=true;
                break;
            }
        }
        return flag;
    }

    @Override
    public Object run() throws ZuulException {
//        获取token
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        try {
            String token = CookieUtils.getCookieValue(request, jwtProperties.getUser().getCookieName());

//解析token
            Payload<UserInfo> payload = JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey(), UserInfo.class);
//        验证token黑名单
            Boolean boo = redisTemplate.hasKey(payload.getId());
            if (boo!=null&&boo){
                throw new LyException(ExceptionEnum.UNAUTHORIZED);
            }
//        获取角色权限
            UserInfo user = payload.getUserInfo();

//            将用户信息存入请求头中
            RequestContext currentContext = RequestContext.getCurrentContext();
            currentContext.addZuulRequestHeader(jwtProperties.getUser().getHeradName(), user.getId().toString());

            String role = user.getRole();
//        获取当前资源路径
            String path = request.getRequestURI();
            String method = request.getMethod();
//        TODO 判断权限，此处暂时空置，等待权限服务完成后补充
            log.info("【网关】用户{},角色{}。访问服务{}:{}",user.getUsername(),role,method,path);
        } catch (Exception e) {
//           拦截请求
            context.setSendZuulResponse(false);
            context.setResponseStatusCode(403);
            log.error("非法访问，未登陆,地址：{}",request.getRemoteHost(),e);
        }
        return null;
    }
}
