package com.douyin.controller.user;

import com.douyin.context.BaseContext;
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
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody ShoppingCart shoppingCart) {
        shoppingCart.setUserId(BaseContext.getCurrentId());
        log.info("添加购物车，商品信息为：{}", shoppingCart);


        return shoppingCartService.addShoppingCart(shoppingCart);
    }

    /**
     * 查看用户购物车数据
     * @return
     */
    @GetMapping("/list")
    public Result list() {
        log.info("查看购物车数据");
        List<ShoppingCart> shoppingCartList = shoppingCartService.list(BaseContext.getCurrentId());
        return Result.success(shoppingCartList);
    }

    /**
     * 删除购物车中的一个商品 数量减一
     * @param shoppingCart
     * @return
     */
    @DeleteMapping("/sub")
    public Result delete(@RequestBody ShoppingCart shoppingCart){
        shoppingCart.setUserId(BaseContext.getCurrentId());
        log.info("删除购物车中的商品:{}",shoppingCart);

        return  shoppingCartService.decreaseProductNum(shoppingCart);
    }

    /**
     * 清空购物车
     * @return
     */
    @DeleteMapping("/clean")
    public Result clean(){
        log.info("清空购物车");

        return shoppingCartService.clean(BaseContext.getCurrentId());
    }
}
