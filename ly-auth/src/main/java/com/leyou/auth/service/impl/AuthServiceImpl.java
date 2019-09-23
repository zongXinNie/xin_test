package com.leyou.auth.service.impl;

import com.leyou.auth.config.JwtProperties;
import com.leyou.auth.entity.AppInfo;
import com.leyou.auth.entity.Application;
import com.leyou.auth.mapper.ApplicationMapper;

import com.leyou.auth.service.AuthService;
import com.leyou.common.ExceptionEnum;
import com.leyou.entity.Payload;
import com.leyou.entity.UserInfo;
import com.leyou.exception.LyException;
import com.leyou.user.client.UserClient;
import com.leyou.user.dto.UserDTO;
import com.leyou.utils.CookieUtils;
import com.leyou.utils.JwtUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Nie ZongXin
 * @date 2019/9/19 14:27
 */
@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserClient userClient;
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private ApplicationMapper applicationMapper;
    private final static String USER_ROLE = "role_user";
    private final static int SECOND = 5000;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    /**
     * 登陆验证，授权
     *
     * @param username
     * @param password
     * @param response
     */
    @Override
    public void login(String username, String password, HttpServletResponse response) {


//        查询用户是否存在
        try {
            UserDTO user = userClient.findUserByUsernameAndPassword(username, password);

//        生成token
            String token = JwtUtils.generateTokenExpireInMinutes(new UserInfo(user.getId(), user.getUsername(), USER_ROLE),
                    jwtProperties.getPrivateKey(), jwtProperties.getUser().getExpire());
//        发送token
            CookieUtils.newCookieBuilder()
                    .response(response)   //respone,用户写cookie
                    .httpOnly(true)        //保证安全防止XSS攻击,不允许JS操作cookie
                    .domain(jwtProperties.getUser().getCookieDomian())  //设置domain
                    .name(jwtProperties.getUser().getCookieName())     //设置cookie名称
                    .value(token)                                    //设置cookie值
                    .build();                                     //写入cookie
        } catch (Exception e) {
            throw new LyException(ExceptionEnum.INVALID_USERNAME_PASSWORD);
        }

    }

    /**
     * 每次客户发起请求，验证用户登入状态,并查看token是否过期
     *
     * @param request
     * @param response
     * @return
     */
    @Override
    public UserInfo verify(HttpServletRequest request, HttpServletResponse response) {
        try {
            String token = CookieUtils.getCookieValue(request, jwtProperties.getUser().getCookieName());
            Payload<UserInfo> payload = JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey(), UserInfo.class);

            Boolean boo = redisTemplate.hasKey(payload.getId());
            if (boo != null && boo) {
                throw new LyException(ExceptionEnum.UNAUTHORIZED);
            }

//           获取过期时间
            Date expiration = payload.getExpiration();
//            获取刷新时间
            DateTime refreshTime = new DateTime(expiration.getTime()).plusMinutes(jwtProperties.getUser().getMinRefreshInterval());
//判断是否过了刷新时间
            if (refreshTime.isBefore(System.currentTimeMillis())) {
//                如果过了刷新时间，则生成新的token
                token = JwtUtils.generateTokenExpireInMinutes(payload.getUserInfo(), jwtProperties.getPrivateKey(), jwtProperties.getUser().getExpire());
                //        发送token
                CookieUtils.newCookieBuilder()
                        .response(response)   //respone,用户写cookie
                        .httpOnly(true)        //保证安全防止XSS攻击,不允许JS操作cookie
                        .domain(jwtProperties.getUser().getCookieDomian())  //设置domain
                        .name(jwtProperties.getUser().getCookieName())     //设置cookie名称
                        .value(token)                                    //设置cookie值
                        .build();                                     //写入cookie
            }
            return payload.getUserInfo();
        } catch (Exception e) {
            throw new LyException(ExceptionEnum.UNAUTHORIZED);
        }
    }

    /**
     * 登出操作
     *
     * @param request
     * @param response
     */
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            String token = CookieUtils.getCookieValue(request, jwtProperties.getUser().getCookieName());
            Payload<UserInfo> payload = JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey(), UserInfo.class);
            UserInfo userInfo = payload.getUserInfo();
            long time = payload.getExpiration().getTime() - System.currentTimeMillis();
//        大于5秒存入redis
            if (time > SECOND) {
                redisTemplate.opsForValue().set(payload.getId(), "", time, TimeUnit.MINUTES);
            }
//        删除cookie
            CookieUtils.deleteCookie(jwtProperties.getUser().getCookieName(), jwtProperties.getUser().getCookieDomian(), response);
        } catch (Exception e) {

            throw new RuntimeException(e);
        }

    }

    /**
     * 微服务认证并申请令牌
     *
     * @param id
     * @param secret
     * @return
     */

    @Override
    public String authoriz(Long id, String secret) {
        Application app = applicationMapper.selectByPrimaryKey(id);
        if (app == null) {
            throw new LyException(ExceptionEnum.INVALID_SERVER_ID_SECRET);
        }
        if (!bCryptPasswordEncoder.matches(secret, app.getSecret())) {
            throw new LyException(ExceptionEnum.INVALID_SERVER_ID_SECRET);
        }
        List<Long> targetIdList = applicationMapper.findTargetIdList(id);
//数据放入荷载中
        AppInfo appInfo = new AppInfo();
        app.setId(id);
        appInfo.setServiceName(app.getServiceName());
        appInfo.setTargetList(targetIdList);

//        生成token返回
        return JwtUtils.generateTokenExpireInMinutes(appInfo, jwtProperties.getPrivateKey(), jwtProperties.getApp().getExpire());

    }


}
