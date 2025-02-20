package com.douyin.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ProductPageQueryDTO implements Serializable {

    //起始页码
    private int page;

    //每页多少
    private int pageSize;

    //查询商品类似名称
    private String name;

    //查询分类id
    private Integer categoryId;

    //查询状态 0表示禁用 1表示启用
    private Integer status;

}
