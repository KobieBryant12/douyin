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

        //查询用户名和密码是否正确
        User u = userService.login(user);

        //用户名和密码正确
        if(u != null){
            if(redisTemplate.opsForValue().get(username) == null){//用户之前未登录
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
            }else{//用户之前已登录
                return Result.success("用户已登录");
            }
        }

        return Result.success("用户名或密码错误");
    }

}
