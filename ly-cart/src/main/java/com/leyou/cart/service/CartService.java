package com.leyou.cart.service;

import com.leyou.cart.entity.Cart;

import java.util.List;

public interface CartService {
    void addCart(Cart cart);

    List<Cart> findCart();

    void deleteCart(String skuId);

    void batchAddCart(List<Cart> carts);

    void addCartNum(String id, Integer num);
}
