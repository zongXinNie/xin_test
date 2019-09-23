package com.leyou.order.mapper;

import com.leyou.mapper.BaseMapper;
import com.leyou.order.entity.OrderDetail;

import java.util.List;

public interface OrderDetailMapper extends BaseMapper<OrderDetail> {
    /**
     * 批量插入订单详情
     * @param orderDetails
     * @return
     */
    int inserDetailList(List<OrderDetail> orderDetails);
}
