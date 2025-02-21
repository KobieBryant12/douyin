package com.douyin.mapper;

import com.douyin.entity.OrderAndDetail;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface OrderMapper {

    /**
     * 创建订单
     * @param orderAndDetail
     */
    @Insert("insert into douyin.order (user_id, order_number, address_id, create_time, pay_method)" +
            " values (#{userId}, #{orderNumber}, #{addressId}, #{createTime}, #{payMethod})")
    void addOrder(OrderAndDetail orderAndDetail);

    /**
     * 根据用户ID和订单号查询订单ID
     * @param orderAndDetail
     * @return
     */
    @Select("select id from douyin.order where user_id = #{userId} and order_number = #{orderNumber}")
    Long getByUserIdAndOrderNumber(OrderAndDetail orderAndDetail);

    /**
     * 根据用户id查询订单
     * @param orderAndDetail
     * @return
     */
    @Select("select id from douyin.order where user_id = #{userId}")
    Long getByUserId(OrderAndDetail orderAndDetail);

    /**
     * 根据订单ID查询订单支付方式
     * @param orderId
     * @return
     */
    @Select("select * from douyin.order where id = #{orderId}")
    Short getPayMethodByOrderId(Long orderId);

    /**
     * 根据订单ID修改订单支付方式
     * @param newPayMethod
     */
    @Update("update douyin.order set pay_method = #{newPayMethod} where id = #{orderId}")
    void updatePayMethod(Short newPayMethod, Long orderId);
}
