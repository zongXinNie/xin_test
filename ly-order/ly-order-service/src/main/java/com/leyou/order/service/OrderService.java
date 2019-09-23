package com.leyou.order.service;

import com.leyou.order.dto.OrderDTO;
import com.leyou.order.vo.OrderDetailVO;
import com.leyou.order.vo.OrderVO;

import java.util.List;

public interface OrderService {
    Long createOrder(OrderDTO orderDTO);

    OrderVO findOrderById(Long id);
}
