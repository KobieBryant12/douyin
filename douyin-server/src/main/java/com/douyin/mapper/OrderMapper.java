package com.douyin.mapper;

import com.douyin.entity.OrderAndDetail;
import com.douyin.entity.ShoppingCart;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;


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
     * 根据用户id和状态查询订单
     * @param orderAndDetail
     * @return 返回订单id
     */
    @Select("select id from douyin.order where user_id = #{userId} and pay_status = 0")
    Long getByUserIdAndStatus(OrderAndDetail orderAndDetail);

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

    /**
     * 根据订单id集合和状态返回订单id
     * @param orderIds
     * @return
     */
    List<Long> getByOrderIdAndStatus(List<Long> orderIds);

    /**
     * 查询用户订单信息
     * @param orderAndDetail
     * @return
     */
    List<OrderAndDetail> list(OrderAndDetail orderAndDetail);
}
