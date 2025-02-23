package com.douyin.service;

import com.douyin.dto.OrdersPaymentDTO;
import com.douyin.entity.OrderAndDetail;
import com.douyin.result.Result;

public interface OrderService {

    /**
     * 创建订单
     * @param orderAndDetail
     * @return
     */
    Result addOrder(OrderAndDetail orderAndDetail);

    /**
     * 根据订单ID修改订单支付方式
     * @param orderId
     */
    void updateOrderPayMethod(Long orderId);

    /**
     * 查询用户的订单信息
     * @param orderAndDetail
     */
    Result listOrder(OrderAndDetail orderAndDetail);

    /**
     * 支付订单
     * @param ordersPaymentDTO
     */
    Integer payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    void paySuccess(OrdersPaymentDTO ordersPaymentDTO);
}
