package com.douyin.mapper;

import com.douyin.entity.OrderAndDetail;
import com.douyin.entity.ShoppingCart;
import com.douyin.entity.SingleOrderDetail;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderDetailMapper {



    /**
     * 创建订单明细表
     * @param orderDetails
     */
    void addOrderDetail(List<SingleOrderDetail> orderDetails);

    /**
     * 查询订单明细
     * @param singleOrderDetail
     * @return 返回订单id
     */
    List<Long> list(SingleOrderDetail singleOrderDetail);
}
