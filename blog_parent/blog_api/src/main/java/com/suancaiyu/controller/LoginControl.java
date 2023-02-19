package com.suancaiyu.controller;

import com.suancaiyu.resultcommon.Result;
import com.suancaiyu.service.LoginService;
import com.suancaiyu.vo.LoginParams;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/login")
public class LoginControl {
    @Resource
    private LoginService loginService;

    /**
     * 登入
     * @param loginParams
     * @return
     */
    @PostMapping()
    public Result Login(@RequestBody LoginParams loginParams){
        return loginService.login(loginParams);
    }
}
