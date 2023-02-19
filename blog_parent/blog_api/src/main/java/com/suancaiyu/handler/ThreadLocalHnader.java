package com.suancaiyu.handler;

import com.alibaba.fastjson.JSON;
import com.suancaiyu.pojo.SysUser;
import com.suancaiyu.utils.UserThreadLocal;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Component
public class ThreadLocalHnader implements HandlerInterceptor {
    @Resource
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)){
            return true;
        }
        String token=request.getHeader("Authorization");
        if (token==null){
            return true;
        }
        String sysUserJson= redisTemplate.opsForValue().get("TOKEN_"+token);
        SysUser sysUser = JSON.parseObject(sysUserJson,SysUser.class);
        //将用户信息放入线程
        UserThreadLocal.put(sysUser);
        return true;
    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //请求结束删除线程内数据
        UserThreadLocal.remove();
    }
}
