package com.douyin.consumer;

import com.douyin.controller.user.OrderController;
import com.douyin.entity.OrderAndDetail;
import com.douyin.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RocketMQMessageListener(consumerGroup = "springBootGroup", topic = OrderController.TOPIC)
@Slf4j
public class OrderConsumer implements RocketMQListener<OrderAndDetail> {

    @Autowired
    private OrderMapper orderMapper;


    @Override
    @Transactional
    public void onMessage(OrderAndDetail orderAndDetail) {
        log.info("Received Order:" + orderAndDetail);

        String orderNumber = orderAndDetail.getOrderNumber();
        OrderAndDetail order = orderMapper.getByNumber(orderNumber);
        if(order.getPayStatus() == (short)0){
            order.setPayStatus((short)2);
            order.setCancelTime(LocalDateTime.now());
            orderMapper.update(order);
        }

        log.info("订单：{}未支付超时取消!", order);
    }
}
