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
     * 根据分类ID查询商品数量
     * @param id
     * @return
     */

    @Select("select count(id) from product where category_id = #{id}")
    Integer countByCategoryId(Long id);
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

    /**
     * 根据商品ID查询商品状态
     * @param productId
     * @return
     */
    @Select("select status from douyin.product where id = #{productId}")
    Integer queryProductStatus(Long productId);

    /**
     * 根据商品Id集合查询是否有停售的商品
     * @param productIds
     * @return
     */
    List<Product> queryProductStatusByProductIds(List<Long> productIds);

    List<Product> list(Product product);
}
