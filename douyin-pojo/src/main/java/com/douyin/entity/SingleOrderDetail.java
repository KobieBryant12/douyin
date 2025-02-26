package com.douyin.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 单条订单明细实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SingleOrderDetail {
    private Long orderId; //订单id

    private Long userId; //用户id

    private Long productId; //商品id

    private String productName; //商品名称

    private Integer number; //商品数量

    private BigDecimal price; //商品单价

}
