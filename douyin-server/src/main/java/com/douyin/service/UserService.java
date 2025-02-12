package com.douyin.service;


import com.douyin.entity.User;

public interface UserService{

    /**
     * 用户注册
     * @param user
     */
    public void register(User user);


    /**
     * 根据ID编辑修改用户基本信息
     * @param user
     */
    void edit(User user);

    /**
     * 根据ID查询用户信息
     * @param id
     * @return
     */
    User getById(Integer id);
}
