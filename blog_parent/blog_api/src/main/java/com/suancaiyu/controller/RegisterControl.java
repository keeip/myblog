package com.suancaiyu.controller;

import com.suancaiyu.resultcommon.Result;
import com.suancaiyu.service.LoginService;
import com.suancaiyu.vo.RegisterParams;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/register")
public class RegisterControl {
    @Resource
    private LoginService loginService;
    /**
     * 注册
     * @param registerParams
     * @return
     */
    @PostMapping()
    public Result register(@RequestBody RegisterParams registerParams){
        return loginService.register(registerParams);
    }
}
