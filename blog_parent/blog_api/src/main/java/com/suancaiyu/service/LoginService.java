package com.suancaiyu.service;

import com.suancaiyu.resultcommon.Result;
import com.suancaiyu.vo.LoginParams;
import com.suancaiyu.vo.RegisterParams;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

public interface LoginService {
    /**
     * 登入service方法，返回token
     * @param loginParams
     * @return
     */
   public Result login(LoginParams loginParams);

    /**
     * 退出登入，redis删除对应的token和用户信息
     * @param token
     * @return
     */
   public Result logout(String token);

    /**
     * 注册service
     * @param registerParams
     * @return
     */
   public Result register(RegisterParams registerParams);
}
