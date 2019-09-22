package com.leyou.filters;

import com.leyou.config.JwtProperties;
import com.leyou.task.PrivilegeTokenHolder;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

/**
 * @author Nie ZongXin
 * @date 2019/9/20 17:11
 */
//拦截请求头，在请求头中加入自身的token,去调用其他微服务
@Component
public class PrivilegeFilter extends ZuulFilter {
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private PrivilegeTokenHolder privilegeTokenHolder;
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    /**
     * PRE_DECORATION_FILTER_ORDER 是Zuul默认的处理请求头的过滤器，我们放到这个之后执行,放到之后执行
     * @return
     */
    @Override
    public int filterOrder() {
        return FilterConstants.PRE_DECORATION_FILTER_ORDER+1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext context = RequestContext.getCurrentContext();
//        将token存入请求头中
        context.addZuulRequestHeader(jwtProperties.getApp().getHeaderName(),privilegeTokenHolder.getToken());
        return null;
    }
}
