package com.suancaiyu.utils;

import com.suancaiyu.pojo.SysUser;

/**
 * 封装线程类，保存用户信息
 */
public class UserThreadLocal {
    private UserThreadLocal(){

    }
    //实例化一个本地线程
    private static final ThreadLocal<SysUser> LOCAL=new ThreadLocal<>();

    /**
     * 放入用户数据进线程里
     * @param sysUser
     */
    public static void put(SysUser sysUser){
        LOCAL.set(sysUser);
    }
    public static SysUser get(){
       return LOCAL.get();
    }
    public static void remove(){
        LOCAL.remove();
    }
}
