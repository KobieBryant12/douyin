package com.douyin.service;

import com.douyin.dto.ShoppingCartDTO;
import com.douyin.entity.ShoppingCart;
import com.douyin.result.Result;

import java.util.List;

public interface ShoppingCartService {

    /**
     * 购物车中添加商品
     * @param shoppingCart
     */
    Result addShoppingCart(ShoppingCart shoppingCart);

    /**
     * 查看用户购物车
     * @return
     */
    List<ShoppingCart> list(Long userId);

    /**
     * 减小购物车中商品数量。若原数量大于1则减一，若等于1则删除
     * @param shoppingCart
     */
    Result decreaseProductNum(ShoppingCart shoppingCart);

    /**
     * 清空购物车
     */
    Result clean(Long userId);
}
