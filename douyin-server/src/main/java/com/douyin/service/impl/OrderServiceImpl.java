package com.douyin.service.impl;

import com.douyin.constant.MessageConstant;
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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

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
        //根据用户id查询是否有默认地址，没有则返回错误信息
        Long userId = orderAndDetail.getUserId();
        Short isDefault = (short)1;
        AddressBook defaultAddress = addressBookMapper.getByUserIdAndDefault(userId, isDefault);
        if(defaultAddress == null){
            return Result.error(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }

        //根据用户id查询是否有未支付的订单，有则返回错误信息
        Long byUserIdAndStatus = orderMapper.getByUserIdAndStatus(orderAndDetail);
        if(byUserIdAndStatus != null){
            return Result.error(MessageConstant.UNPROCESSED_ORDER);
        }

        //创建订单表，地址id设置为用户的默认地址
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

    /**
     * 查询用户的订单信息
     * @param orderAndDetail
     * @return 返回用户每个订单的订单明细集合
     */
    @Override
    public Result listOrder(OrderAndDetail orderAndDetail) {
        //根据用户id和订单id查询得到订单id集合
        List<OrderAndDetail> orderAndDetailList = orderMapper.list(orderAndDetail);

        //再根据订单id集合查询订单明细
        List<OrderAndDetail> orderAndDetails = new ArrayList<>();
        for(int i = 0; i < orderAndDetailList.size(); i++){
            Long orderId = orderAndDetailList.get(i).getId();
            List<SingleOrderDetail> singleOrderDetails = orderDetailMapper.listByOrderId(orderId);
            SingleOrderDetail[] singleOrderDetailsArray = singleOrderDetails.toArray(new SingleOrderDetail[singleOrderDetails.size()]);
            OrderAndDetail oad = new OrderAndDetail();
            BeanUtils.copyProperties(orderAndDetailList.get(i), oad);
            oad.setSingleOrderDetails(singleOrderDetailsArray);
            orderAndDetails.add(oad);
        }

        return Result.success(orderAndDetails);
    }


}
