package com.douyin.mapper;

import com.douyin.entity.ShoppingCart;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {
    List<ShoppingCart> list(ShoppingCart shoppingCart);


    @Update("update douyin.shopping_cart set number = #{number} where id = #{id}")
    void updateNumberById(ShoppingCart shoppingCart);

    @Insert("insert into douyin.shopping_cart(name, price, user_id, product_id, number,  create_time, update_time)" +
            "values (#{name}, #{price}, #{userId}, #{productId}, #{number}, #{createTime}, #{updateTime})")
    void insert(ShoppingCart shoppingCart);

    /**
     * 根据id修改商品数量
     * @param shoppingCart
     */
    @Update("update douyin.shopping_cart set number=#{number} where id=#{id}")
    void update(ShoppingCart shoppingCart);

    /**
     * 删除购物车中的一个商品
     * @param shoppingCart
     */
    void delete(ShoppingCart shoppingCart);
}
