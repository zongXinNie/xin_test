package com.leyou.auth.controller;

import com.leyou.auth.service.AuthService;
import com.leyou.entity.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Nie ZongXin
 * @date 2019/9/19 14:25
 */
@RestController
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestParam("username") String username,
                                      @RequestParam("password") String password,
                                      HttpServletResponse response) {
        authService.login(username, password, response);
        return ResponseEntity.ok().build();
    }

    /**
     * 验证用户信息
     *
     * @param request
     * @param response
     * @return
     */
    @GetMapping("verify")
    public ResponseEntity<UserInfo> verify(HttpServletRequest request, HttpServletResponse response) {
        return ResponseEntity.ok(authService.verify(request, response));
    }

    /**
     * 注销登陆
     * @param request
     * @param response
     * @return
     */
    @PostMapping("logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response){
        authService.logout(request,response);
        return ResponseEntity.ok().build();
    }
}
