package com.douyin.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCart implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long productId;

    private String name;

    private Long userId;

    private Integer number;

    private BigDecimal price;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
