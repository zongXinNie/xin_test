package com.leyou.cart.intercept;

import com.leyou.common.ExceptionEnum;
import com.leyou.exception.LyException;
import com.leyou.threadLoads.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Nie ZongXin
 * @date 2019/9/21 22:20
 */

public class UserIntercept implements HandlerInterceptor {
    private static final String HERADNAME="user_info";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        获取用户id,存入当前线程
        String userId = request.getHeader(HERADNAME);
        if (StringUtils.isBlank(userId)){
            throw new LyException(ExceptionEnum.UNAUTHORIZED);
        }
        UserHolder.setUser(Long.parseLong(userId));
        return true;
    }
}
