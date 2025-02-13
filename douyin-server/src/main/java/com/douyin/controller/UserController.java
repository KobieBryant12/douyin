package com.douyin.controller;


import com.douyin.entity.User;
import com.douyin.result.Result;
import com.douyin.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 注册用户
     * @param user
     * @return
     */
    @PostMapping("/register")
    public Result registerUser(@RequestBody User user){
        log.info("注册用户:{}", user);

        userService.register(user);
        return Result.success();
    }

    /**
     * 修改编辑用户
     * @return
     */
    @PutMapping("edit")
    public Result editUser(@RequestBody User user){
        log.info("修改编辑用户：{}", user);
        userService.edit(user);
        return Result.success();
    }


    /**
     * 根据ID查询用户基本信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result getUserById(@PathVariable Integer id){
        log.info("查询id为 {} 的用户信息", id);
        User user = userService.getById(id);
        return Result.success(user);
    }
}
