package com.leyou.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Nie ZongXin
 * @date 2019/9/22 22:17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    @NotNull
    private Long addressId; //收货人地址ID
    @NotNull
    private Integer paymentType;  //付款类型
    @NotNull
    private List<CartDTO> carts; //订单详情
}
