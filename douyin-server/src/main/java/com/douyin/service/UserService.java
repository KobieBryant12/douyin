package com.douyin.service;


import com.douyin.dto.ChangePwd;
import com.douyin.entity.User;
import com.douyin.result.Result;

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
    User getById(Long id);

    /**
     * 根据ID删除用户
     * @param id
     */
    void deleteUser(Long id);

    /**
     * 用户登录
     * @param user
     * @return
     */
    User login(User user);


    /**
     * 根据ID启用或禁用用户
     * @param id
     */
    void setUserStatusById(Short oldStatus, Long id);

    /**
     * 根据ID修改用户密码
     * @param changePwd
     */
    Result changeUserPwd(ChangePwd changePwd);

    /**
     * 根据用户ID充值
     * @param user
     */
    void topUpByUserId(User user);
}
