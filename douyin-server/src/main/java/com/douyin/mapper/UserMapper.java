package com.douyin.mapper;

import com.douyin.dto.ChangePwd;
import com.douyin.entity.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    /**
     * 用户注册，添加用户
     * @param user
     */
    @Insert("insert into douyin.user(username, name, password, phone, gender, avatar, create_time, update_time) " +
            "values(#{username}, #{name}, #{password}, #{phone}, #{gender}, #{avatar}, #{createTime}, #{updateTime})")
    void insert(User user);

    /**
     * 根据ID修改用户基本信息
     * @param user
     */
    void update(User user);


    /**
     * 根据ID查询用户信息
     * @param id
     * @return
     */
    @Select("select id, username, name, phone, gender, avatar, create_time, status, cash from douyin.user where id = #{id}")
    User getById(Long id);


    /**
     * 根据ID删除用户
     * @param id
     */
    @Delete("delete from douyin.user where id = #{id}")
    void deleteById(Long id);

    /**
     * 根据用户名和密码查询用户
     * @param user
     * @return
     */
    @Select("select * from douyin.user where username = #{username} and password = #{password}")
    User getByUsernamAndPassword(User user);


    /**
     * 根据ID或用户名设置用户状态
     * @param newStatus
     * @param id
     */
    @Update("update douyin.user set status = #{newStatus} where id = #{id}")
    void setUserStatus(Short newStatus, Long id);

    /**
     * 根据ID和密码查询用户 用于修改密码时确认用户
     * @param id, password
     * @return
     */
    @Select("select * from douyin.user where id = #{id} and password = #{password}")
    User getByIdAndPassword(@Param("id") Long id, @Param("password") String password);

    /**
     * 根据ID修改用户密码
     * @param user
     */
    @Update("update douyin.user set password = #{password}, update_time = #{updateTime} where id = #{id}")
    void updatePwdById(User user);
}
