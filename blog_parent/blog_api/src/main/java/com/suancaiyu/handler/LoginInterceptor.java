package com.suancaiyu.handler;

import com.alibaba.fastjson.JSON;
import com.suancaiyu.pojo.SysUser;
import com.suancaiyu.resultcommon.Result;
import com.suancaiyu.utils.JwtUtils;
import com.suancaiyu.utils.UserThreadLocal;
import com.suancaiyu.vo.ErrorCode;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    @Resource
    private RedisTemplate<String,String> redisTemplate;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /**
         * 1 判断请求路径是否为handlermethod(也就是controller),静态资源放行
         * 2 判断token是否为null，为null 返回错误信息
         */
        if (!(handler instanceof HandlerMethod)){
            return true;
        }
        String token=request.getHeader("Authorization");
        log.info("==========request start===========");
        String requestURI=request.getRequestURI();//获取请求路径，（去除ip host的路径）
        log.info("request uri:{}",requestURI);
        log.info("request method:{}",request.getMethod());
        log.info("token:{}", token);
        log.info("=================request end===========================");
        if (token==null){
            Result result=Result.fail(ErrorCode.NO_LOGIN.getCode(), ErrorCode.NO_LOGIN.getMsg());
            response.setContentType("application/json;charset=utf-8");//设置返回数据类型,为json字符串类型
            response.getWriter().print(JSON.toJSON(result));//返回json字符串
            return false;//不放行
        }
        //token不为null则进行验证
       Map<String,Object> claims=JwtUtils.checkToken(token);
        log.info("{}",claims);
       String sysUserJson= redisTemplate.opsForValue().get("TOKEN_"+token);
        log.info("{}",sysUserJson);
       if (claims==null||sysUserJson==null){
          Result result=Result.fail(ErrorCode.NO_LOGIN.getCode(), ErrorCode.NO_LOGIN.getMsg());
           response.setContentType("application/json;charset=utf-8");//设置返回数据类型,为json字符串类型
           response.getWriter().print(JSON.toJSON(result));//返回json字符串
           return false;//不放行
       }
       //将json字符串解析为java对象
         SysUser sysUser =JSON.parseObject(sysUserJson,SysUser.class);
        //登录验证成功，放行
        //我希望在controller中 直接获取用户的信息 怎么获取?


       return true;
    }

}
