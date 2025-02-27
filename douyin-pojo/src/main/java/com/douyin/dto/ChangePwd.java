package com.douyin.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 修改用户密码实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePwd {
    private Long id; //用户id
    private String userName; //用户名
    private String password; //修改前的密码
    private String newPassword; //修改后的密码
    private String confirmPwd; //确认修改的密码
}
