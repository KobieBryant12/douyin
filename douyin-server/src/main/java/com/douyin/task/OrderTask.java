package com.douyin.task;


import com.douyin.entity.OrderAndDetail;
import com.douyin.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 定时任务类
 */
@Component
@Slf4j
public class OrderTask {
    @Autowired
    private OrderMapper orderMapper;
    /**
     * 超时订单处理
     */
    @Scheduled(cron = "0/5 * * * * ? ")
    public void outOfTime() {
        log.info("订单超时，定时处理", LocalDateTime.now());
        List<OrderAndDetail> ordersList = orderMapper.getByStatusOrderTimeLT((short)0, LocalDateTime.now().plusMinutes(-1));
        if(ordersList != null && ordersList.size() > 0){
            for (OrderAndDetail orders : ordersList) {
                orders.setPayStatus((short)2);
                orders.setCancelTime(LocalDateTime.now());
                orderMapper.update(orders);//更新订单状态
            }
        }

    }

//    /**
//     * 定时处理一直派送的订单
//     */
//    @Scheduled(cron = "0 0 1 * * ? ")  //每天凌晨一点
//    public void processDeliveryOrder() {
//        List<Orders> ordersList = orderMapper.getByStatusOrderTimeLT(Orders.DELIVERY_IN_PROGRESS, LocalDateTime.now().plusMinutes(-60));
//        if(ordersList != null && ordersList.size() > 0){
//            for (Orders orders : ordersList) {
//                orders.setStatus(Orders.COMPLETED);
//                orders.setCancelReason("订单完成");
//                orders.setCancelTime(LocalDateTime.now());
//                orderMapper.update(orders);//更新订单状态
//            }
//        }
//    }

}
