package com.leyou.cart.controller;

import com.leyou.cart.entity.Cart;
import com.leyou.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Nie ZongXin
 * @date 2019/9/21 22:38
 */
@RestController
public class CartController {

    @Autowired
    private CartService cartService;

    /**
     * 添加商品到后台购物车
     *
     * @param cart
     * @return
     */
    @PostMapping()
    public ResponseEntity<Void> addCart(@Valid @RequestBody Cart cart) {
        cartService.addCart(cart);
        return ResponseEntity.ok().build();
    }

    /**
     * 查询购物车
     *
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity<List<Cart>> findCart() {
        return ResponseEntity.ok(cartService.findCart());
    }

    /**
     * 删除购物车中的商品
     *
     * @param skuId
     * @return
     */
    @DeleteMapping("{skuId}")
    public ResponseEntity<Void> deleteCart(@PathVariable("skuId") String skuId) {
        cartService.deleteCart(skuId);
        return ResponseEntity.ok().build();
    }

    /**
     * 批量添加购物车商品
     *
     * @param carts
     * @return
     */
    @PostMapping("/list")
    public ResponseEntity<Void> batchAddCart(@Valid @RequestBody List<Cart> carts) {
        cartService.batchAddCart(carts);
        return ResponseEntity.ok().build();
    }

    /**
     * 购物商品增减数量
     *
     * @param id
     * @param num
     * @return
     */
    @PutMapping()
    public ResponseEntity<Void> addCartNum(String id, Integer num) {
        cartService.addCartNum(id, num);
        return ResponseEntity.ok().build();
    }
}
