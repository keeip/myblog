package com.suancaiyu.config;

import com.suancaiyu.handler.LoginInterceptor;
import com.suancaiyu.handler.ThreadLocalHnader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.Arrays;

@Configuration
public class webConfig implements WebMvcConfigurer {
    /**
     *
     * @param registry
     *解决跨域问题
     */
    @Resource
    private LoginInterceptor loginInterceptor;
    @Resource
    private ThreadLocalHnader threadLocalHnader;
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("http://localhost:8080");
    }
    //添加拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/publish").addPathPatterns("/logout").addPathPatterns("/create/change");
        registry.addInterceptor(threadLocalHnader)
                .addPathPatterns("/**").excludePathPatterns("/login").excludePathPatterns("/register");
    }
}
