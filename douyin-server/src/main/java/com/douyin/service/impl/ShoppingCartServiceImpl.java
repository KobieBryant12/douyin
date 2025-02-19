package com.douyin.service.impl;

import com.douyin.context.BaseContext;
import com.douyin.dto.ShoppingCartDTO;
import com.douyin.entity.Product;
import com.douyin.entity.ShoppingCart;
import com.douyin.mapper.ProductMapper;
import com.douyin.mapper.ShoppingCartMapper;
import com.douyin.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private ProductMapper productMapper;

    @Override
    public void addShoppingCart(ShoppingCart shoppingCart) {
       // ShoppingCart shoppingCart = new ShoppingCart();
        //BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        // TODO 设计一个ThreadLocal，登录时将user_id存进去，再在此处将user_id设置到ShoppingCart中
        shoppingCart.setUserId(1L);
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);

        //如果已经存在，将数量加一
        if (list != null && list.size() > 0) {
            ShoppingCart cart = list.get(0);
            cart.setNumber(cart.getNumber() + 1);
            shoppingCartMapper.updateNumberById(cart);
        } else {
            //如果不存在，需要插入一条购物车数据
            Long productId =  shoppingCart.getProductId();
            Product product = productMapper.getById(productId);
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCart.setUpdateTime(LocalDateTime.now());

        }
        shoppingCartMapper.insert(shoppingCart);
    }

    /**
     * 获取用户购物车数据
     *
     * @return
     */
    @Override
    public List<ShoppingCart> list() {
        ShoppingCart shoppingCart = new ShoppingCart();
        //shoppingCart.setUserId(BaseContext.getCurrentId());
        shoppingCart.setUserId(1L);
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);

        return list;
    }

    /**
     * 删除购物车中的商品
     *
     * @param shoppingCartDTO
     */
    @Override
    public void delete(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        //shoppingCart.setUserId(BaseContext.getCurrentId());
        shoppingCart.setUserId(1L);

        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        Integer number = list.get(0).getNumber();
        if (number > 1) {//数量大于1,将数量-1
            list.get(0).setNumber(number-1);

            shoppingCartMapper.update(list.get(0));
        } else {//数量小于1，在购物车中删除商品
            shoppingCartMapper.delete(list.get(0));
        }

    }

    /**
     * 清空购物车
     */
    @Override
    public void clean() {
        ShoppingCart shoppingCart = new ShoppingCart();
        //shoppingCart.setUserId(BaseContext.getCurrentId());
        shoppingCart.setUserId(1L);
        shoppingCartMapper.delete(shoppingCart);
    }
}
