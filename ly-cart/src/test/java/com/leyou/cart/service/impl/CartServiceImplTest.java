package com.leyou.cart.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CartServiceImplTest {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    public void addCart() {
        BoundHashOperations<String, String, String> map = redisTemplate.boundHashOps("test:id:1");
       /* if (map==null){
            redisTemplate.opsForHash().put("test:id:1","1","测试");
        }*/
//        Map<String, String> entries = map.entries();
        map.put("2","测试");
    }
}