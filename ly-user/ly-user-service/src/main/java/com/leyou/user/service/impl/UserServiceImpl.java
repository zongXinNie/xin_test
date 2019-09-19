package com.leyou.user.service.impl;

import com.leyou.common.ExceptionEnum;
import com.leyou.constants.AbstractMQConstants;
import com.leyou.constants.RegexPatterns;
import com.leyou.exception.LyException;
import com.leyou.user.dto.UserDTO;
import com.leyou.user.entity.User;
import com.leyou.user.mapper.UserMapper;
import com.leyou.user.service.UserService;
import com.leyou.user.utils.CodecUtils;
import com.leyou.utils.BeanHelper;
import com.leyou.utils.RegexUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author Nie ZongXin
 * @date 2019/9/17 21:50
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private AmqpTemplate amqpTemplate;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * 校验用户名或手机号是否重复
     *
     * @param data
     * @param type
     * @return
     */
    @Override
    public Boolean checkData(String data, Integer type) {
        if (StringUtils.isBlank(data) || type == null) {
            throw new LyException(ExceptionEnum.INVALID_PARAM_ERROR);
        }

        User user = new User();

        switch (type) {
            case 1:
                user.setUsername(data);
                break;
            case 2:
                user.setPhone(data);
                break;
            default:
                throw new LyException(ExceptionEnum.INVALID_PARAM_ERROR);
        }
        int count = userMapper.selectCount(user);
        return count == 0;
    }

    /**
     * 根据手机发送验证码
     *
     * @param phone
     */
    @Override
    public void sendMessage(String phone) {
        if (!RegexUtils.isPhone(phone)) {
            throw new LyException(ExceptionEnum.INVALID_PHONE_NUMBER);
        }
        String validateCode = RandomStringUtils.randomNumeric(6);

//        发送验证码之前，首先判断一下5分钟内，是否已经发送，如已发送，让用户5分钟之后再试
        String phoneCode = redisTemplate.opsForValue().get(phone);
        if (StringUtils.isNotBlank(phoneCode)) {
            throw new LyException(ExceptionEnum.SEND_MESSAGE_REPEAT);
        }
//        防止接口被恶意调用，对同一个手机号在一天内接收短信的次数做出限制
        phoneCode = redisTemplate.opsForValue().get("sms:send:" + phone);
        if (StringUtils.isBlank(phoneCode)) {
            redisTemplate.opsForValue().set("sms:send:" + phone, "0", 24, TimeUnit.HOURS);
            phoneCode = "0";
        }
        if (Integer.parseInt(phoneCode) > 3) {
            throw new LyException(ExceptionEnum.SEND_MESSAGE_SPILL);
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("phone", phone);
        map.put("code", validateCode);
//        发送短信
        amqpTemplate.convertAndSend(AbstractMQConstants.Exchange.SMS_EXCHANGE_NAME,
                AbstractMQConstants.RoutingKey.VERIFY_CODE_KEY, map);
//对每个手机号码发送的次数做出统计
        redisTemplate.opsForValue().increment("sms:send:" + phone);
//        验证码存入数据库，设置超时时间,5分钟
        redisTemplate.opsForValue().set(phone, validateCode, 5, TimeUnit.MINUTES);

    }

    /**
     * 用户注册
     *
     * @param code
     * @param user
     */
    @Override
    public void userRegister(User user, String code) {

//        从缓存库中获取验证码
        String validateCode = redisTemplate.opsForValue().get(user.getPhone());
//      验证短信验证码
        if (!StringUtils.equals(validateCode, code)) {
            throw new LyException(ExceptionEnum.INVALID_VERIFY_CODE);
        }
//        写入数据库
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        int insertSelective = userMapper.insertSelective(user);
        if (insertSelective != 1) {
            throw new LyException(ExceptionEnum.INSERT_OPERATION_FAIL);
        }
    }

    /**
     * 根据用户名和密码查询用户
     *
     * @param username
     * @param password
     * @return
     */
    @Override
    public UserDTO findUserByUsernameAndPassword(String username, String password) {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            throw new LyException(ExceptionEnum.INVALID_PARAM_ERROR);
        }
        User u = new User();
        u.setUsername(username);
        User user = userMapper.selectOne(u);
//        用户名不存在
        if (user == null) {
            throw new LyException(ExceptionEnum.INVALID_USERNAME_PASSWORD);
        }
//密码校验
        if (!passwordEncoder.matches(password, user.getPassword())) {
//            密码错误
            throw new LyException(ExceptionEnum.INVALID_USERNAME_PASSWORD);
        }
        return BeanHelper.copyProperties(user, UserDTO.class);
    }
}
