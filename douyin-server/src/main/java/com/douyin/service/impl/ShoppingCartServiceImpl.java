package com.douyin.service.impl;

import com.douyin.constant.MessageConstant;
import com.douyin.dto.ShoppingCartDTO;
import com.douyin.entity.OrderAndDetail;
import com.douyin.entity.Product;
import com.douyin.entity.ShoppingCart;
import com.douyin.entity.SingleOrderDetail;
import com.douyin.mapper.OrderDetailMapper;
import com.douyin.mapper.OrderMapper;
import com.douyin.mapper.ProductMapper;
import com.douyin.mapper.ShoppingCartMapper;
import com.douyin.result.Result;
import com.douyin.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.ReactiveSubscription;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private ProductMapper productMapper;


    @Transactional
    @Override
    public Result addShoppingCart(ShoppingCart shoppingCart) {
        //如果该商品停售不可加入购物车
        Integer status = productMapper.queryProductStatus(shoppingCart.getProductId());
        if(status == 0){
            return Result.error(MessageConstant.SUSPENSION_OF_SALES);
        }

        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);

        //如果已经存在，将数量加一
        if (list != null && list.size() > 0) {
            ShoppingCart cart = list.get(0);
            cart.setNumber(cart.getNumber() + 1);
            cart.setUpdateTime(LocalDateTime.now());
            shoppingCartMapper.updateNumberById(cart);
        } else {
            //如果不存在，需要插入一条购物车数据
            Long productId =  shoppingCart.getProductId();
            Product product = productMapper.getById(productId);
            shoppingCart.setNumber(1);
            shoppingCart.setPrice(product.getPrice());
            shoppingCart.setName(product.getName());
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCart.setUpdateTime(LocalDateTime.now());
            shoppingCartMapper.insert(shoppingCart);
        }
        return Result.success();
    }

    /**
     * 获取用户购物车数据
     *
     * @return
     */
    @Override
    public List<ShoppingCart> list(Long userId) {
        List<ShoppingCart> list = shoppingCartMapper.listByUserId(userId);

        return list;
    }

    /**
     * 减小购物车中的商品数量
     * 若原数量大于1则减1，若等于1则删除
     *
     * @param shoppingCart
     */
    @Transactional
    @Override
    public Result decreaseProductNum(ShoppingCart shoppingCart) {
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        Integer number = list.get(0).getNumber();
        if (number > 1) {//数量大于1,将数量-1
            list.get(0).setNumber(number-1);
            shoppingCartMapper.update(list.get(0));
        } else {//数量小于1，在购物车中删除商品
            shoppingCartMapper.delete(list.get(0));
        }
        return Result.success();
    }

    /**
     * 根据用户id清空购物车
     */
    @Transactional
    @Override
    public Result clean(Long userId) {
        shoppingCartMapper.cleanById(userId);
        return Result.success();
    }
}
