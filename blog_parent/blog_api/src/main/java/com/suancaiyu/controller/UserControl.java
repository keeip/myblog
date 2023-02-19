package com.suancaiyu.controller;

import com.suancaiyu.resultcommon.Result;
import com.suancaiyu.service.SysUserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/users")
public class UserControl {
    @Resource
    private SysUserService sysUserService;

    /**
     * 通过token返回用户信息
     * @return
     */
    @GetMapping("/currentUser")
    public Result currentUser(@RequestHeader("Authorization") String token){
        return sysUserService.FindByToken(token);
    }
}
