package com.leyou.order.service.impl;

import com.leyou.item.client.ItemClient;
import com.leyou.common.ExceptionEnum;
import com.leyou.dto.SkuDTO;
import com.leyou.exception.LyException;
import com.leyou.order.dto.CartDTO;
import com.leyou.order.dto.OrderDTO;
import com.leyou.order.entity.Order;
import com.leyou.order.entity.OrderDetail;
import com.leyou.order.entity.OrderLogistics;
import com.leyou.order.enums.OrderStatusEnum;
import com.leyou.order.mapper.OrderDetailMapper;
import com.leyou.order.mapper.OrderLogisticsMapper;
import com.leyou.order.mapper.OrderMapper;
import com.leyou.order.service.OrderService;
import com.leyou.order.vo.OrderDetailVO;
import com.leyou.order.vo.OrderLogisticsVO;
import com.leyou.order.vo.OrderVO;
import com.leyou.threadLoads.UserHolder;
import com.leyou.user.client.UserClient;
import com.leyou.user.dto.AddressDTO;
import com.leyou.utils.BeanHelper;
import com.leyou.utils.IdWorker;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Nie ZongXin
 * @date 2019/9/22 22:25
 */
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private ItemClient itemClient;
    @Autowired
    private UserClient userClient;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private OrderLogisticsMapper orderLogisticsMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long createOrder(OrderDTO orderDTO) {
//       订单商品详情表

//        生成订单ID
        long orderId = idWorker.nextId();
//        用户id
        Long userId = UserHolder.getUser();

        List<CartDTO> carts = orderDTO.getCarts();
        Map<Long, Integer> skuMap = carts.stream().collect(Collectors.toMap(CartDTO::getSkuId, CartDTO::getNum));
//
       /* ArrayList<Long> skuIds = new ArrayList<>();
        for (CartDTO cart : carts) {
            skuIds.add(cart.getSkuId());
        }*/
        List<Long> skuIds = orderDTO.getCarts().stream().map(CartDTO::getSkuId).collect(Collectors.toList());
        List<SkuDTO> skus = itemClient.findSkuByIds(skuIds);
        ArrayList<OrderDetail> orderDetails = new ArrayList<>();
        Integer totalMoney = 0;  //总金额
        for (SkuDTO skuDTO : skus) {
            Integer num = skuMap.get(skuDTO.getId());
            totalMoney += skuDTO.getPrice().intValue() * num;//总金额
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setSkuId(skuDTO.getId());
            orderDetail.setNum(num);
            orderDetail.setTitle(skuDTO.getTitle());
            orderDetail.setOwnSpec(skuDTO.getOwnSpec());
            orderDetail.setPrice(skuDTO.getPrice().longValue());
            orderDetail.setImage(StringUtils.substringBefore(skuDTO.getImages(), ","));
            orderDetails.add(orderDetail);
        }

//        获取地址信息
        AddressDTO addressDTO = userClient.queryAddressById(userId, orderDTO.getAddressId());
        OrderLogistics orderLogistics = new OrderLogistics();
        orderLogistics.setOrderId(orderId);           //订单ID
        orderLogistics.setAddressee(addressDTO.getAddressee());//收件人
        orderLogistics.setPhone(addressDTO.getPhone());//手机号码
        orderLogistics.setProvince(addressDTO.getProvince());//省
        orderLogistics.setCity(addressDTO.getCity()); //城市
        orderLogistics.setDistrict(addressDTO.getDistrict());//区
        orderLogistics.setStreet(addressDTO.getStreet());//街道
        orderLogistics.setPostcode(Long.parseLong(addressDTO.getZipCode()));//邮编

//        订单信息
        Order order = new Order();
        order.setOrderId(orderId);
        order.setTotalFee(totalMoney.longValue());
        order.setPaymentType(orderDTO.getPaymentType());
        order.setUserId(userId);
        order.setActualFee(totalMoney.longValue());
        order.setStatus(OrderStatusEnum.INIT.value());


        if (orderMapper.insertSelective(order) != 1) {
            throw new LyException(ExceptionEnum.INSERT_OPERATION_FAIL);
        }
        orderDetailMapper.insertList(orderDetails);
        if (orderLogisticsMapper.insertSelective(orderLogistics) != 1) {
            throw new LyException(ExceptionEnum.INSERT_OPERATION_FAIL);
        }
//        减少库存
        itemClient.minusStock(skuMap);
        return orderId;
    }

    /**
     * 根据订单id查询订单信息
     * @param id
     * @return
     */
    @Override
    public OrderVO findOrderById(Long id) {
        OrderDetail orderDetail1 = new OrderDetail();
        orderDetail1.setOrderId(id);
        List<OrderDetail> orderDetails = orderDetailMapper.select(orderDetail1);
        if (orderDetails==null||orderDetails.size()<=0){
            throw new LyException(ExceptionEnum.ORDER_NOT_FOUND);
        }
        Order order = orderMapper.selectByPrimaryKey(id);
        if (order==null){
            throw new LyException(ExceptionEnum.ORDER_NOT_FOUND);
        }
        OrderLogistics orderLogistics = orderLogisticsMapper.selectByPrimaryKey(id);
        if (orderLogistics==null){
            throw new LyException(ExceptionEnum.ORDER_NOT_FOUND);
        }
        OrderVO orderVO = BeanHelper.copyProperties(order, OrderVO.class);
        orderVO.setDetailList(BeanHelper.copyWithCollection(orderDetails,OrderDetailVO.class));
        orderVO.setLogistics(BeanHelper.copyProperties(orderLogistics, OrderLogisticsVO.class));
        return orderVO;
    }
}
