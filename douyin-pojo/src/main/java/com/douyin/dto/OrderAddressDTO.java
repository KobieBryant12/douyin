package com.douyin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderAddressDTO {
    private Long userId; //用户id
    private String orderNumber; //订单号
    private Long newAddressId; //新地址id
}
