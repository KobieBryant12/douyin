package com.douyin.service.impl;

import com.douyin.entity.AddressBook;
import com.douyin.entity.OrderAndDetail;
import com.douyin.entity.ShoppingCart;
import com.douyin.entity.SingleOrderDetail;
import com.douyin.mapper.AddressBookMapper;
import com.douyin.mapper.OrderDetailMapper;
import com.douyin.mapper.OrderMapper;
import com.douyin.mapper.ShoppingCartMapper;
import com.douyin.result.Result;
import com.douyin.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.Vector;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private AddressBookMapper addressBookMapper;

    /**
     * 创建订,成功返回订单ID
     * @param orderAndDetail
     * @return orderId
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result addOrder(OrderAndDetail orderAndDetail) {
        //根据用户id查询是否有默认地址，没有则返回
        Long userId = orderAndDetail.getUserId();
        Short isDefault = (short)1;
        AddressBook defaultAddress = addressBookMapper.getByUserIdAndDefault(userId, isDefault);
        if(defaultAddress == null){
            return Result.error("没有收货地址，无法下单!");
        }

        Long byUserId = orderMapper.getByUserId(orderAndDetail);
        if(byUserId != null){
            return Result.error("还有订单未处理！");
        }
        //创建订单表
        orderAndDetail.setCreateTime(LocalDateTime.now());
        orderAndDetail.setAddressId(defaultAddress.getId());
        String orderNumber = UUID.randomUUID().toString() + orderAndDetail.getUserId() + "";
        orderAndDetail.setOrderNumber(orderNumber);
        orderMapper.addOrder(orderAndDetail);

        //得到创建的订单的ID
        Long orderId = orderMapper.getByUserIdAndOrderNumber(orderAndDetail);


        //创建一个订单明细集合
        orderAndDetail.setId(orderId);
        SingleOrderDetail[] singleOrderDetails = orderAndDetail.getSingleOrderDetails();
        for(int i = 0; i < singleOrderDetails.length; i++){
            singleOrderDetails[i].setOrderId(orderId);
            singleOrderDetails[i].setUserId(orderAndDetail.getUserId());
            ShoppingCart byUserIdAndProductId = shoppingCartMapper.getByUserIdAndProductId(singleOrderDetails[i].getProductId(), singleOrderDetails[i].getUserId());
            singleOrderDetails[i].setPrice(byUserIdAndProductId.getPrice());
            singleOrderDetails[i].setNumber(byUserIdAndProductId.getNumber());
        }
        List<SingleOrderDetail> orderDetails = Arrays.asList(singleOrderDetails);


        //创建订单明细表
        orderDetailMapper.addOrderDetail(orderDetails);
        return Result.success(orderId);
    }

    /**
     * 根据订单ID修改订单支付方式
     * @param orderId
     */
    @Override
    public void updateOrderPayMethod(Long orderId) {
        Short payMethod = orderMapper.getPayMethodByOrderId(orderId);
        Short newPayMethod = (short)1;
        if(payMethod == (short)2 ){
            orderMapper.updatePayMethod(newPayMethod, orderId);
        }else{
            newPayMethod = (short)2;
            orderMapper.updatePayMethod(newPayMethod, orderId);
        }
    }


}
