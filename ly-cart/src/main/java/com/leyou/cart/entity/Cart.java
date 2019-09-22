package com.leyou.cart.entity;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author Nie ZongXin
 * @date 2019/9/21 22:32
 */
@Data
public class Cart {
    @NotNull
    private Long skuId;  // 商品id
    @NotNull
    private String title;  // 标题
    private String image;  // 图片
    @Min(0)
    private Long price;  // 加入购物车时的价格
    @Min(0)
    private Integer num;  // 购买数量
    @NotNull
    private String ownSpec;  // 商品规格参数
}
