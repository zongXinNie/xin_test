package com.leyou.user.service;

import com.leyou.user.dto.UserDTO;
import com.leyou.user.entity.User;

public interface UserService {
    Boolean checkData(String data, Integer type);

    void sendMessage(String phone);

    void userRegister(User user, String code);

    UserDTO findUserByUsernameAndPassword(String username, String password);
}
