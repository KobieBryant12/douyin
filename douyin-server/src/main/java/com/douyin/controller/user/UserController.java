package com.douyin.controller.user;


import com.douyin.anno.Log;
import com.douyin.context.BaseContext;
import com.douyin.dto.ChangePwd;
import com.douyin.entity.User;
import com.douyin.result.Result;
import com.douyin.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;


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
    @Log
    @PutMapping("edit")
    public Result editUser(@RequestBody User user){
        user.setId(BaseContext.getCurrentId());

        log.info("修改编辑用户：{}", user);
        userService.edit(user);
        return Result.success();
    }

    /**
     * 根据ID查询用户基本信息
     * @param
     * @return
     */
    @GetMapping("/query")
    public Result getUserById(){
        log.info("查询id为 {} 的用户信息", BaseContext.getCurrentId());

        User user = userService.getById(BaseContext.getCurrentId());
        return Result.success(user);
    }

    /**
     * 根据ID删除用户
     * @param
     * @return
     */
    @Log
    @DeleteMapping("/delete")
    public Result deleteUser(){
        log.info("删除用户:{}", BaseContext.getCurrentId());

        userService.deleteUser(BaseContext.getCurrentId());
        return Result.success();
    }

    /**
     * 用户退出
     * @param
     * @return
     */
    @GetMapping("/exit")
    public Result exit(){
        log.info("用户：{} 退出登录", BaseContext.getCurrentId());

        //销毁该用户在redis中的jwt令牌
        String stringId = BaseContext.getCurrentId().toString();
        redisTemplate.delete(stringId);
        return Result.success();
    }


    /**
     * 根据ID启用禁用账号
     * @return
     */
    @GetMapping("/alterStatus/{id}")
    public Result enableAndDisable(@PathVariable Long id){
        User user = userService.getById(id);
        Short oldStatus = user.getStatus();

        //如果是1，则禁用用户，如果是0，则启用用户
        String newStatus = "启用";
        if(oldStatus == (short)1){
            newStatus = "禁用";
        }
        log.info("{} 用户：{}", newStatus, id);

        userService.setUserStatusById(oldStatus, id);

        //如果是禁用，则销毁redis中该用户的jwt令牌
        String stringId = user.getId().toString();
        if(oldStatus == (short)1) {
            redisTemplate.delete(stringId);
        }

        return Result.success();
    }

    /**
     * 根据ID修改用户密码
     * @param changePwd
     * @return
     */
    @Log
    @PostMapping("/changepwd")
    public Result changePassword(@RequestBody ChangePwd changePwd){
        changePwd.setId(BaseContext.getCurrentId());
        log.info("修改id为 {} 的用户的密码", changePwd.getId());

        return userService.changeUserPwd(changePwd);
    }

    /**
     * 根据用户ID充值
     * @param user
     * @return
     */
    @Log
    @PostMapping("/topUp")
    public Result topUp(@RequestBody User user){
        user.setId(BaseContext.getCurrentId());

        log.info("用户id: {} 充值：{}", user.getId(), user.getCash());
        userService.topUpByUserId(user);
        return Result.success();
    }
}
