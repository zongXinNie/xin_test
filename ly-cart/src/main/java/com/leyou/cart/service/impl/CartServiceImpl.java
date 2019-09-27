package com.leyou.cart.service.impl;

import com.leyou.cart.entity.Cart;
import com.leyou.cart.service.CartService;
import com.leyou.common.ExceptionEnum;
import com.leyou.exception.LyException;
import com.leyou.threadLoads.UserHolder;
import com.leyou.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Nie ZongXin
 * @date 2019/9/21 22:41
 */
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private StringRedisTemplate redisTemplate;
    private static final String KEY_PREFIX = "ly:cart:uid:";

    /**
     * 添加商品进入后台购物车
     *
     * @param cart
     */
    @Override
    public void addCart(Cart cart) {
        String userId = KEY_PREFIX + UserHolder.getUser();
        BoundHashOperations<String, String, String> userMap = redisTemplate.boundHashOps(userId);
        addCartRedis(cart, userMap);
    }

    /**
     * 添加商品进入购物车
     *
     * @param cart
     * @param userMap
     */
    private void addCartRedis(Cart cart, BoundHashOperations<String, String, String> userMap) {

        String cartId = cart.getSkuId().toString();
        Integer num = cart.getNum();

        Boolean boo = userMap.hasKey(cartId);
        if (boo != null && boo) {
            cart = JsonUtils.toBean(userMap.get(cartId), Cart.class);
            cart.setNum(num + cart.getNum());
        }
        userMap.put(cartId, JsonUtils.toString(cart));
    }

    /**
     * 查询购物车集合
     *
     * @return
     */
    @Override
    public List<Cart> findCart() {
        String userId = KEY_PREFIX + UserHolder.getUser();
        Boolean boo = redisTemplate.hasKey(userId);
        if (boo == null || !boo) {
            throw new LyException(ExceptionEnum.CARTS_NOT_FOUND);
        }
        BoundHashOperations<String, String, String> userMap = redisTemplate.boundHashOps(userId);
        if (userMap.size() == null || userMap.size() < 0) {
            throw new LyException(ExceptionEnum.CARTS_NOT_FOUND);
        }
        List<String> cartString = userMap.values();

        //查看购物车数据
        return cartString.stream().map(json -> JsonUtils.toBean(json, Cart.class)).collect(Collectors.toList());
    }

    /**
     * 删除购物车中商品
     *
     * @param skuId
     */
    @Override
    public void deleteCart(String skuId) {
        String userId = KEY_PREFIX + UserHolder.getUser();
        BoundHashOperations<String, String, String> userMap = redisTemplate.boundHashOps(userId);
        Boolean boo = userMap.hasKey(skuId);
        if (boo != null && boo) {
            userMap.delete(skuId);
        }

    }

    /**
     * 批量添加购物车
     *
     * @param carts
     */
    @Override
    public void batchAddCart(List<Cart> carts) {
        String userId = KEY_PREFIX + UserHolder.getUser();
        BoundHashOperations<String, String, String> userMap = redisTemplate.boundHashOps(userId);
        for (Cart cart : carts) {
            addCartRedis(cart, userMap);
        }

    }

    /**
     * 购物商品增减数量
     *
     * @param id
     * @param num
     */
    @Override
    public void addCartNum(String id, Integer num) {
        String userId = KEY_PREFIX + UserHolder.getUser();
        BoundHashOperations<String, String, String> userMap = redisTemplate.boundHashOps(userId);
        Cart cart = JsonUtils.toBean(userMap.get(id), Cart.class);
        cart.setNum(num);
        userMap.put(id, JsonUtils.toString(cart));
    }
}
