package com.douyin.mapper;


import com.douyin.entity.SingleOrderDetail;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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

    /**
     * 根据订单id集合查询对应订单明细
     * @param orderId
     * @return 订单明细集合
     */
    @Select("select * from douyin.order_detail where order_id = #{orderId}")
    List<SingleOrderDetail> listByOrderId(Long orderId);

    /**
     * 根据用户id删除订单明细记录
     * @param id
     */
    @Delete("delete from douyin.order_detail where user_id = #{id}")
    void deleteByUserId(Long id);

    /**
     * 根据userId和productId集合查询订单号集合
     * @return
     */
    List<Long> listByUserIdsAndProductIds(List<SingleOrderDetail> detailsList);
}
