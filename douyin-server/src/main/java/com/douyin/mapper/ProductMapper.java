package com.douyin.mapper;

import com.douyin.dto.ProductPageQueryDTO;
import com.douyin.entity.Product;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProductMapper {

    /**
     * 插入商品
     * @param product
     */
    void insert(Product product);

    /**
     * 分页查询
     * @param productPageQueryDTO
     * @return
     */
    Page<Product> pageQuery(ProductPageQueryDTO productPageQueryDTO);

    /**
     * 根据商品ID查询商品信息
     * @param productId
     * @return
     */
    @Select("select * from douyin.product where id = #{productId}")
    Product getById(Long productId);

    /**
     * 根据id批量删除商品
     * @param ids
     */
    void deleteByIds(List<Long> ids);

    /**
     * 修改商品
     * @param product
     */
    void update(Product product);
}
