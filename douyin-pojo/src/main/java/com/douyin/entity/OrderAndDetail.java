package com.douyin.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 订单和订单明细的实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderAndDetail {
    private Long id; //订单id

    private Long userId; //用户id

    private String orderNumber; //订单号，用UUID实现

    private Long addressId; //地址id

    private SingleOrderDetail[] singleOrderDetails; //订单明细数组对象

    private LocalDateTime createTime; //创建订单时间

    private LocalDateTime cancelTime; //取消订单时间

    private LocalDateTime checkoutTime; //支付订单时间

    private Integer payMethod; //支付方式 ：1微信支付 2支付宝支付

    private Short payStatus; //支付状态 ：0未支付 1支付订单 2取消订单
}
