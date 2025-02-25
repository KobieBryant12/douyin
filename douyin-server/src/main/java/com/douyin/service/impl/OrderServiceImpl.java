package com.douyin.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.douyin.constant.MessageConstant;
import com.douyin.context.BaseContext;
import com.douyin.dto.OrdersPaymentDTO;
import com.douyin.entity.*;
import com.douyin.exception.OrderBusinessException;
import com.douyin.mapper.*;
import com.douyin.result.Result;
import com.douyin.service.OrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ProductMapper productMapper;

    /**
     * 创建订,成功返回订单ID
     * @param orderAndDetail
     * @return orderId
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result addOrder(OrderAndDetail orderAndDetail) {
        //查询要创建订单的商品中是否有停售的，如果有返回错误信息
        SingleOrderDetail[] products = orderAndDetail.getSingleOrderDetails();
        List<Long> productIds = new ArrayList<>();
        for(int i = 0; i < products.length; i++){
            productIds.add(products[i].getProductId());
        }
        List<Product> products1 = productMapper.queryProductStatusByProductIds(productIds);
        if(products1 != null && products1.size() != 0){
            return Result.error(MessageConstant.SUSPENSION_OF_SALES);
        }

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

    /**
     * 支付订单
     * @param ordersPaymentDTO
     * @return 返回支付状态
     */
    public Integer payment(OrdersPaymentDTO ordersPaymentDTO) {
        String orderNum = ordersPaymentDTO.getOrderNumber();
        OrderAndDetail order =  orderMapper.getByNumber(orderNum);
        //首先看一下订单状态是否是未支付
        Short payStatus = order.getPayStatus();
        //订单已经支付，返回-1
        if (payStatus == (short)1) return -1;

        //订单已被取消 返回-2
        if (payStatus == (short)2) return -2;

        //计算总金额
        List<SingleOrderDetail> singleOrderDetails = orderDetailMapper.listByOrderId(order.getId());
        BigDecimal totalPrice = new BigDecimal(0);
        for (SingleOrderDetail singleOrderDetail : singleOrderDetails){
            int num = singleOrderDetail.getNumber();
            BigDecimal price = singleOrderDetail.getPrice();
            totalPrice = totalPrice.add(price.multiply(new BigDecimal(num)));
        }

        //比较用户余额与订单总价 余额不足返回0
        Long userId = order.getUserId();
        User user = userMapper.getById(userId);
        BigDecimal cash = user.getCash();
        if (cash.compareTo(totalPrice) < 0) { //cash < totalPrice
            return 0;
        }

        //支付成功返回1
        cash = cash.subtract(totalPrice);
        user.setCash(cash);
        userMapper.update(user);
        return 1;
    }


    /**
     * 支付成功回调函数，更新订单状态，删除订单中在购物车里对应的商品
     * @param ordersPaymentDTO
     */
    public void paySuccess(OrdersPaymentDTO ordersPaymentDTO) {

        String orderNum = ordersPaymentDTO.getOrderNumber();

        // 根据订单号查询订单
        OrderAndDetail order = orderMapper.getByNumber(orderNum);
        order.setPayStatus((short) 1);
        order.setCheckoutTime(LocalDateTime.now());
        //更新订单状态
        orderMapper.update(order);

        //删除订单中在购物车里对应的商品
        List<SingleOrderDetail> singleOrderDetails = orderDetailMapper.listByOrderId(order.getId());
        ShoppingCart shoppingCart = new ShoppingCart();
        for (SingleOrderDetail singleOrderDetail : singleOrderDetails){
            Long productId =  singleOrderDetail.getProductId();
            shoppingCart.setUserId(order.getUserId());
            shoppingCart.setProductId(productId);
            shoppingCartMapper.delete(shoppingCart);
        }

    }

    /**
     * 根据订单号UUID取消支付
     * @param orderNum
     */
    @Transactional
    @Override
    public Result cancelOrderByNum(String orderNum) {
        // 根据订单号查询订单
        //如果订单状态为1、2返回错误信息
        OrderAndDetail order = orderMapper.getByNumber(orderNum);
        if (order.getPayStatus() == (short)1)
            return Result.error(MessageConstant.ORDER_PAID);
        if(order.getPayStatus() == (short)2)
            return Result.error(MessageConstant.ORDER_CANCELLED);

        order.setPayStatus((short) 2);
        order.setCancelTime(LocalDateTime.now());
        //更新订单状态
        orderMapper.update(order);

        //根据用户id和商品id删除订单中在购物车里对应的商品
        List<SingleOrderDetail> singleOrderDetails = orderDetailMapper.listByOrderId(order.getId());
        ShoppingCart shoppingCart = new ShoppingCart();
        for (SingleOrderDetail singleOrderDetail : singleOrderDetails){
            Long productId =  singleOrderDetail.getProductId();
            shoppingCart.setUserId(order.getUserId());
            shoppingCart.setProductId(productId);
            shoppingCartMapper.delete(shoppingCart);
        }
        return Result.success();
    }

}
