package com.douyin.controller;



import com.douyin.entity.OrderAndDetail;
import com.douyin.result.Result;
import com.douyin.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/user/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 创建订单
     * @return orderId
     */
    @PostMapping("/add")
    public Result addOrder(@RequestBody OrderAndDetail orderAndDetail){
        log.info("用户：{} 创建订单", orderAndDetail.getUserId());

        return orderService.addOrder(orderAndDetail);
    }

    /**
     * 根据订单id修改订单支付方式
     * @return
     */
    @PutMapping("/paymethod/{orderId}")
    public Result alterPayMethod(@PathVariable Long orderId){
        log.info("修改订单：{} 的支付方式", orderId);

        orderService.updateOrderPayMethod(orderId);
        return Result.success();
    }

    /**
     * 查询用户的订单
     * @return 返回用户的订单明细集合
     */
    @PostMapping("/query")
    public Result listOrder(@RequestBody OrderAndDetail orderAndDetail){
        log.info("查询用户：{} 的订单信息", orderAndDetail.getUserId());

        return orderService.listOrder(orderAndDetail);
    }
}
