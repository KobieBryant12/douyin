package com.douyin.service;

import com.douyin.dto.ShoppingCartDTO;
import com.douyin.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {

    void addShoppingCart(ShoppingCart shoppingCart);

    /**
     * 查看用户购物车
     * @return
     */
    List<ShoppingCart> list();

    /**
     * 删除购物车中的商品
     * @param shoppingCartDTO
     */
    void delete(ShoppingCartDTO shoppingCartDTO);

    /**
     * 清空购物车
     */
    void clean();
}
