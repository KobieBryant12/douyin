package com.douyin.controller.user;



import com.douyin.context.BaseContext;
import com.douyin.dto.OrderAddressDTO;
import com.douyin.dto.OrdersPaymentDTO;
import com.douyin.entity.OrderAndDetail;
import com.douyin.result.Result;
import com.douyin.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@RestController
@RequestMapping("/user/order")
public class OrderController {

    public final static String TOPIC = "OrderTopic01";

    @Autowired
    private OrderService orderService;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 创建订单
     * @return orderId
     */
    @PostMapping("/add")
    public Result addOrder(@RequestBody OrderAndDetail orderAndDetail){
        orderAndDetail.setUserId(BaseContext.getCurrentId());
        log.info("用户：{} 创建订单", orderAndDetail.getUserId());

        Result result = orderService.addOrder(orderAndDetail);
        Message<OrderAndDetail> msg = MessageBuilder.withPayload(orderAndDetail).build();
        //向MQ发送异步消息
        rocketMQTemplate.asyncSend(TOPIC, msg, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("延迟消息投递成功，当前时间：{}", LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE));
            }

            @Override
            public void onException(Throwable throwable) {
                log.info("消息投递失败：{}，错误信息：{}", orderAndDetail, throwable.getMessage());
            }
        }, 3000, 5);

        return result;
    }


    /**
     * 查询用户的订单
     * @return 返回用户的订单明细集合
     */
    @PostMapping("/query")
    public Result listOrder(){
        OrderAndDetail orderAndDetail = new OrderAndDetail();
        orderAndDetail.setUserId(BaseContext.getCurrentId());
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

    /**
     * 根据订单号取消支付
     * @param orderNum
     * @return
     * @throws Exception
     */
    @PutMapping("/cancel/{orderNum}")
    public Result cancel(@PathVariable("orderNum") String orderNum) throws Exception {
        log.info("取消订单：{}", orderNum);

        return orderService.cancelOrderByNum(orderNum);
    }

    /**
     * 修改订单地址
     * @param orderAddressDTO
     * @return
     */
    @PostMapping("/address")
    public Result updateAddress(@RequestBody OrderAddressDTO orderAddressDTO){
        orderAddressDTO.setUserId(BaseContext.getCurrentId());
        log.info("用户id:{} 修改订单号为：{}的订单地址", orderAddressDTO.getUserId(), orderAddressDTO.getOrderNumber());

        return orderService.updateAddress(orderAddressDTO);
    }
}
