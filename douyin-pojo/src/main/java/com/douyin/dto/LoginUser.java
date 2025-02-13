package com.douyin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录时的用户实体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUser {
    private String username;
    private String password;
}
