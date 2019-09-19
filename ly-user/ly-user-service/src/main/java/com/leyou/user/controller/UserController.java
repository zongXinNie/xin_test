package com.leyou.user.controller;

import com.leyou.common.ExceptionEnum;
import com.leyou.exception.LyException;
import com.leyou.user.dto.UserDTO;
import com.leyou.user.entity.User;
import com.leyou.user.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author Nie ZongXin
 * @date 2019/9/17 21:50
 */
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 校验用户名或手机号是否重复
     *
     * @param data
     * @param type  1用户名 2手机
     * @return
     */
    @GetMapping("check/{data}/{type}")
//    描述一个类或者接口
    @ApiOperation(value = "校验用户名数据是否可用，如果不存在则可用")
//    HTTP响应整体描述
    @ApiResponses({
//            HTTP响应其中一个描述
            @ApiResponse(code = 200,message = "校验结果有效,true或false代表可用或不可用"),
            @ApiResponse(code = 400,message = "请求参数有误,比如type不是指定值")
    })                                          //单个参数描述
    public ResponseEntity<Boolean> checkData(@ApiParam(value = "要校验的数据",example = "lisi") @PathVariable("data") String data,
                                             @ApiParam(value = "数据类型,1：用户名，2：手机号",example = "1")@PathVariable("type") Integer type) {
        return ResponseEntity.ok(userService.checkData(data, type));
    }

    /**
     * 发送短信验证码
     *
     * @param phone
     * @return
     */
    @ApiOperation(value = "发送短信验证码")
    @PostMapping("/code")
    public ResponseEntity sendMessage(@ApiParam(value = "接收短信的手机号码")@RequestParam("phone") String phone) {
        userService.sendMessage(phone);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 注册
     *
     * @return
     */
    @ApiOperation("用户注册")
    @PostMapping("register")
    public ResponseEntity userRegister(@ApiParam("用户的注册信息")@Valid User user,
                                       @ApiParam("验证码信息")@RequestParam("code") String code) {

        userService.userRegister(user, code);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 根据用户和密码查询用户
     * @param username
     * @param password
     * @return
     */
    @ApiOperation("根据用户名和密码在数据库查询用户")
    @GetMapping("query")
    public ResponseEntity<UserDTO> findUserByUsernameAndPassword(@ApiParam("用户名")@RequestParam("username") String username,
                                                                 @ApiParam("密码信息")@RequestParam("password") String password) {
        return ResponseEntity.ok(userService.findUserByUsernameAndPassword(username, password));
    }

}
