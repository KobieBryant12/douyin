package com.douyin.service.impl;

import com.douyin.entity.User;
import com.douyin.mapper.UserMapper;
import com.douyin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            user.setPassword("123456");
        }
        userMapper.insert(user);
    }

    @Override
    public void edit(User user) {
        user.setUpdateTime(LocalDateTime.now());
        userMapper.update(user);
    }

    @Override
    public User getById(Integer id) {
        return userMapper.getById(id);
    }
}
