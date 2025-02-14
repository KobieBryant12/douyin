package com.douyin.service.impl;

import com.douyin.entity.User;
import com.douyin.mapper.UserMapper;
import com.douyin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserMapper userMapper;


    @Override
    public void register(User user) {
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        if(user.getPassword() == null || user.getPassword() == ""){
            user.setPassword("e10adc3949ba59abbe56e057f20f883e");//默认密码123456，MD5加密
        }else{
            user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));//MD5加密
        }
        userMapper.insert(user);
    }

    @Override
    public void edit(User user) {
        user.setUpdateTime(LocalDateTime.now());
        userMapper.update(user);
    }

    @Override
    public User getById(Long id) {
        return userMapper.getById(id);
    }

    @Override
    public void deleteUser(Long id) {
        userMapper.deleteById(id);
    }


    @Override
    public User login(User user) {
        return userMapper.getByUsernamAndPassword(user);
    }


    @Override
    public void setUserStatusById(Short oldStatus, Long id) {
        Short newStatus = (short)1;
        if (oldStatus == (short)1){
            newStatus = (short)0;
        }
        userMapper.setUserStatus(newStatus, id);
    }
}
