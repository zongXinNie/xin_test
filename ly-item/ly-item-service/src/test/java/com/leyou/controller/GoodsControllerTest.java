package com.leyou.controller;

import com.leyou.service.GoodsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GoodsControllerTest {
    @Autowired
    private GoodsController goodsController;

    @Test
    public void minusStock() {
        HashMap<Long, Integer> hashMap = new HashMap<>();
        hashMap.put(27359021590L, 10);
        hashMap.put(4938564L,10);
        goodsController.minusStock(hashMap);
    }
}