package com.douyin.controller;

import com.douyin.dto.LoginUser;
import com.douyin.entity.User;
import com.douyin.result.Result;
import com.douyin.service.UserService;
import com.douyin.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 用户登录
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping("/login")
    public Result login(@RequestBody LoginUser loguser){
        String username = loguser.getUsername();
        log.info("用户:{} 登录", username);

        User user = new User();
        user.setUsername(username);
        user.setPassword(loguser.getPassword());

        User u = userService.login(user);

        //登录成功  生成令牌 下发令牌
        if(u != null){
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", u.getId());
            claims.put("name", u.getName());
            claims.put("username", u.getUsername());

            String jwt = JwtUtils.generateJwt(claims); //jwt包含了当前登录用户的信息

            //将jwt令牌和对应的用户名存入redis中，key为username，value为对应的jwt，并设置相同的有效期
            redisTemplate.opsForValue().set(u.getUsername(), jwt, 12, TimeUnit.HOURS);
            String o = (String) redisTemplate.opsForValue().get(u.getUsername());
            System.out.println(o);
            return Result.success(jwt);
        }

        return Result.success("用户名或密码错误");
    }

}
