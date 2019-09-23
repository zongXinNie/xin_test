package com.leyou.user.client;

import com.leyou.user.dto.AddressDTO;
import com.leyou.user.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Nie ZongXin
 * @date 2019/9/19 12:18
 */
@FeignClient(name = "user-service")
public interface UserClient {
    /**
     * 查询用户
     * @param username
     * @param password
     * @return
     */
    @GetMapping("/query")
    UserDTO findUserByUsernameAndPassword(@RequestParam("username") String username, @RequestParam("password") String password);
    /**
     * 根据
     * @param userId 用户id
     * @param id 地址id
     * @return 地址信息
     */
    @GetMapping("/address")
    AddressDTO queryAddressById(@RequestParam("userId") Long userId, @RequestParam("id") Long id);
}
