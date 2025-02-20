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

    //单独查询
    @Select("select * from product where id = #{id}")
    Product getById(Long id);

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
