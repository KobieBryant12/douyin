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
    List<ShoppingCart> list(Long userId);

    /**
     * 减小购物车中商品数量。若原数量大于1则减一，若等于1则删除
     * @param shoppingCart
     */
    void decreaseProductNum(ShoppingCart shoppingCart);

    /**
     * 清空购物车
     */
    void clean(Long userId);
}
