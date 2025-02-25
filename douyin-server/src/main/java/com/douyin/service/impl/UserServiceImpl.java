package com.douyin.service.impl;

import com.douyin.dto.ChangePwd;
import com.douyin.entity.SingleOrderDetail;
import com.douyin.entity.User;
import com.douyin.mapper.AddressBookMapper;
import com.douyin.mapper.OrderDetailMapper;
import com.douyin.mapper.OrderMapper;
import com.douyin.mapper.UserMapper;
import com.douyin.result.Result;
import com.douyin.service.ShoppingCartService;
import com.douyin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private AddressBookMapper addressBookMapper;

    @Autowired
    private RedisTemplate redisTemplate;

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

    @Transactional
    @Override
    public void deleteUser(Long id) {
        User user = userMapper.getById(id);

        //删除用户的订单明细记录和订单表记录
        SingleOrderDetail singleOrderDetail = new SingleOrderDetail();
        singleOrderDetail.setUserId(id);
        List<Long> list = orderDetailMapper.list(singleOrderDetail);
        if(list != null && list.size() != 0) {
            orderDetailMapper.deleteByUserId(id);
            orderMapper.deleteByUserId(id);
        }

        //删除用户的购物车表记录
        shoppingCartService.clean(id);

        //删除用户的地址表记录
        addressBookMapper.deleteByUserId(id);

        //删除用户的同时将用户在redis中的jwt删除
        redisTemplate.delete(user.getUsername());

        //根据用户id删除用户表中的记录
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

    @Override
    public Result changeUserPwd(ChangePwd changePwd) {
        User user = userMapper.getByIdAndPassword(changePwd.getId(), DigestUtils.md5DigestAsHex(changePwd.getPassword().getBytes()));

        if(user == null){
            return Result.error("当前用户密码错误!");
        }

        if(changePwd.getNewPassword() == null || changePwd.getNewPassword() == ""){
            return Result.error("输入的密码不能为空！");
        }

        if(!changePwd.getNewPassword().equals(changePwd.getConfirmPwd())){
            return Result.error("两次输入密码不一致！");
        }

        if(changePwd.getPassword().equals(changePwd.getNewPassword())){
            return Result.error("新密码不能与旧密码相同！");
        }

        user.setPassword(DigestUtils.md5DigestAsHex(changePwd.getNewPassword().getBytes()));//MD5加密
        user.setUpdateTime(LocalDateTime.now());
        userMapper.updatePwdById(user);

        //修改完密码后，删除该用户在redis中的jwt令牌
        redisTemplate.delete(user.getUsername());

        return Result.success();
    }

    @Transactional
    @Override
    public void topUpByUserId(User user) {
        //先找到该id的用户余额
        User oldUser = userMapper.getById(user.getId());
        BigDecimal oldCash = oldUser.getCash();

        //将之前的余额与充值的相加
        BigDecimal addCash = user.getCash();
        BigDecimal newCash = oldCash.add(addCash);

        //更新余额
        User newUser = new User();
        newUser.setId(oldUser.getId());
        newUser.setCash(newCash);
        userMapper.update(newUser);
    }
}
