package com.douyin.service;

import com.douyin.dto.ProductDTO;
import com.douyin.dto.ProductPageQueryDTO;
import com.douyin.entity.Product;
import com.douyin.result.PageResult;

import java.util.List;

public interface ProductService {

    /**
     * 新增商品
     * @param productDTO
     */
    void save(ProductDTO productDTO);

    /**
     * 商品分页查询
     * @param productPageQueryDTO
     * @return
     */
    PageResult pageQuery(ProductPageQueryDTO productPageQueryDTO);

    /**
     * 批量删除商品
     * @param ids
     */
    void deleteBatch(List<Long> ids);

    /**
     * 修改商品
     * @param productDTO
     */
    void update(ProductDTO productDTO);/*
     * 根据分类id查询商品
     */

    List<Product> list(Product product);
}
