package com.leyou.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Nie ZongXin
 * @date 2019/9/22 22:18
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CartDTO {
    private Long skuId;
    private Integer num;
}
