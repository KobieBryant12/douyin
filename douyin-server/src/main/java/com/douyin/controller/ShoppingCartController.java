package com.douyin.controller;

import com.douyin.dto.ShoppingCartDTO;
import com.douyin.entity.ShoppingCart;
import com.douyin.result.Result;
import com.douyin.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/shoppingCart")
@Slf4j
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;


    /**
     * 添加购物车
     * @param shoppingCartDTO
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody ShoppingCart shoppingCart) {
        log.info("添加购物车，商品信息为：{}", shoppingCart);
        shoppingCartService.addShoppingCart(shoppingCart);
        return Result.success();
    }

    /**
     * 查看用户购物车数据
     * @return
     */
    @GetMapping("/list")
    public Result list() {
        log.info("查看购物车数据");
        List<ShoppingCart> shoppingCartList = shoppingCartService.list();
        return Result.success(shoppingCartList);
    }

    /**
     * 删除购物车中的一个商品
     * @param shoppingCartDTO
     * @return
     */
    @PostMapping("/sub")
    public Result delete(@RequestBody ShoppingCartDTO shoppingCartDTO){
        log.info("删除购物车中的商品:{}",shoppingCartDTO);
        shoppingCartService.delete(shoppingCartDTO);

        return Result.success();
    }

    /**
     * 清空购物车
     * @return
     */
    @DeleteMapping("/clean")
    public Result clean(){
        log.info("清空购物车");
        shoppingCartService.clean();

        return Result.success();
    }
}
