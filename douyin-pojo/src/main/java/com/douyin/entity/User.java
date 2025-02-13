package com.douyin.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户实体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id; //ID
    private String username; //用户名
    private String name; //姓名
    private String password;//密码
    private String phone; //手机号
    private Short gender; //性别：1：男，2：女
    private String avatar; //头像url
    private LocalDateTime createTime; //注册时间
    private LocalDateTime updateTime; //修改时间
    private Short status; //用户状态 1：启用 2：禁用
    private Double cash; //用户余额
}
