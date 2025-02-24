package com.douyin.controller;



import com.douyin.dto.OrdersPaymentDTO;
import com.douyin.entity.OrderAndDetail;
import com.douyin.result.Result;
import com.douyin.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
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
     * 查询用户的订单
     * @return 返回用户的订单明细集合
     */
    @PostMapping("/query")
    public Result listOrder(@RequestBody OrderAndDetail orderAndDetail){
        log.info("查询用户：{} 的订单信息", orderAndDetail.getUserId());

        return orderService.listOrder(orderAndDetail);
    }

    /**
     *订单支付
     * 支付成功返回1，否则返回0
     */
    @PutMapping("/payment")
    @Transactional
    public Result payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        int paymentStatus = orderService.payment(ordersPaymentDTO);
        //如果支付成功，修改订单状态为已支付，并删除订单中在购物车里对应的商品
        if (paymentStatus == 1){
            orderService.paySuccess(ordersPaymentDTO);
            return Result.success();
        } else if (paymentStatus == -1) {
            return Result.error("订单已支付，请勿重复支付！");
        } else if (paymentStatus == -2) {
            return Result.error("订单已被取消！");
        } else{
            return Result.error("余额不足，请充值");
        }
    }
}
