package com.leyou.order.interceptor;

import com.leyou.common.ExceptionEnum;
import com.leyou.exception.LyException;
import com.leyou.threadLoads.UserHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Nie ZongXin
 * @date 2019/9/22 19:56
 */
public class UserInterceptor implements HandlerInterceptor {
    private static final String HEADER_NAME = "user_info";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userId = request.getHeader(HEADER_NAME);
        if (StringUtils.isBlank(userId)) {
            throw new LyException(ExceptionEnum.UNAUTHORIZED);
        }
        UserHolder.setUser(Long.parseLong(userId));
        return true;
    }
}
