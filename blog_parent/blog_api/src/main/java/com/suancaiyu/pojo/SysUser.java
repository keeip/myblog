package com.suancaiyu.pojo;

import lombok.Data;

@Data
public class SysUser {
    private Long id;

    private String account;
    //是否为管理员
    private Integer admin;

    private String avatar;

    private Long createDate;

    private Integer deleted;

    private String email;

    private Long lastLogin;

    private String mobilePhoneNumber;
    //  昵称
    private String nickname;

    private String password;
    //加密盐
    private String salt;
    //状态
    private String status;
}
