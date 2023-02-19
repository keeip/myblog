package com.suancaiyu.service;

import com.suancaiyu.pojo.SysUser;
import com.suancaiyu.resultcommon.Result;
import com.suancaiyu.vo.UserVo;

public interface SysUserService {
    /**
     * 根据作者id查询用户
     * @param authorId
     * @return
     */
    public SysUser FindByAuthorId(long authorId);

    /**
     * 根据用户名密码查询用户
     * @param account
     * @param password
     * @return
     */
    public SysUser FindUser(String account, String password);

    /**
     * 通过token返回用户信息
     * @param token
     * @return
     */
    public Result FindByToken(String token);

    /**
     * 通过账号查询用户名
     * @param account
     * @return
     */
    public SysUser FindByAccount(String account);

    void save(SysUser sysUser);

    public UserVo FindById(Long authorId);
}
