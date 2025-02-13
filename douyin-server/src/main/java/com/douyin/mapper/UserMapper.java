package com.douyin.mapper;

import com.douyin.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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
    @Select("select username, name, phone, gender, avatar, create_time, cash from douyin.user where id = #{id}")
    User getById(Integer id);


}
