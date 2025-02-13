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
     * 根据ID查询用户基本信息
     * @param id
     * @return
     */
    User getById(Integer id);

    /**
     * 根据ID删除用户
     * @param id
     */
    void deleteUser(Integer id);

    /**
     * 用户登录
     * @param user
     * @return
     */
    User login(User user);
}
