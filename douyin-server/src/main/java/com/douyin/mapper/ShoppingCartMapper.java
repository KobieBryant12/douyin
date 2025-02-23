package com.douyin.mapper;

import com.douyin.entity.ShoppingCart;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {


    List<ShoppingCart> list(ShoppingCart shoppingCart);

    /**
     * 根据用户id查询购物车
     * @param userId
     * @return
     */
    @Select("select * from douyin.shopping_cart where user_id=#{userId}")
    List<ShoppingCart> listByUserId(Long userId);

    /**
     * 根据用户ID和商品ID查询购物车
     * @return
     */
    @Select("select * from douyin.shopping_cart where product_id = #{productId} and user_id = #{userId}")
    ShoppingCart getByUserIdAndProductId(@Param("productId") Long productId, @Param("userId") Long userId);


    @Update("update douyin.shopping_cart set number = #{number}, update_time=#{updateTime}  where id = #{id}")
    void updateNumberById(ShoppingCart shoppingCart);

    @Insert("insert into douyin.shopping_cart(name, price, user_id, product_id, number,  create_time, update_time)" +
            "values (#{name}, #{price}, #{userId}, #{productId}, #{number}, #{createTime}, #{updateTime})")
    void insert(ShoppingCart shoppingCart);

    /**
     * 根据id修改商品数量
     * @param shoppingCart
     */
    @Update("update douyin.shopping_cart set number=#{number}, update_time=#{updateTime} where id=#{id}")
    void update(ShoppingCart shoppingCart);

    /**
     * 删除购物车中的一个商品
     * @param shoppingCart
     */
    void delete(ShoppingCart shoppingCart);

    /**
     * 清空购物车
     * @param userId
     */
    @Delete("delete from douyin.shopping_cart where user_id=#{userId}")
    void cleanById(Long userId);

    @Delete("delete from douyin.shopping_cart where product_id=#{productId}")
    void deleteByProductId(Long productId);
}
