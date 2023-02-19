package com.suancaiyu.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.suancaiyu.mapper.SysUserMapper;
import com.suancaiyu.pojo.SysUser;
import com.suancaiyu.resultcommon.Result;
import com.suancaiyu.service.SysUserService;
import com.suancaiyu.utils.JwtUtils;
import com.suancaiyu.vo.ErrorCode;
import com.suancaiyu.vo.LoginUserVo;
import com.suancaiyu.vo.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class SysUserServiceImpl implements SysUserService {
    @Resource
    private RedisTemplate<String,String> redisTemplate;
    @Resource
    private SysUserMapper sysUserMapper;
    @Override
    public SysUser FindByAuthorId(long authorId) {
        return sysUserMapper.selectById(authorId);
    }

    @Override
    public SysUser FindUser(String account, String password) {
        LambdaQueryWrapper<SysUser> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysUser::getAccount,account);//加条件
        lambdaQueryWrapper.eq(SysUser::getPassword,password);
        lambdaQueryWrapper.select(SysUser::getAccount,SysUser::getId,SysUser::getNickname,SysUser::getAvatar);//查询账号和id还有nickname
        lambdaQueryWrapper.last("limit 1");//加快查询速度
        return sysUserMapper.selectOne(lambdaQueryWrapper);

    }

    @Override
    public Result FindByToken(String token) {

        /**
         * 1 判断token是否合法,不合法返回错误信息
         * 2 token合法，使用token在redis查找用户信息
         * 3 如果查不到，说明token过期或者伪造token，返回错误信息
         * 4 将查到的用户信息json穿转化为用户对象
         * 5 将用户对象转化为SysUserVo对象并且返回
         */
        Map<String,Object> cliams= JwtUtils.checkToken(token);
        if (cliams==null){
            return Result.fail(ErrorCode.TOKEN_ILLEGAL.getCode(),ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }
        String userJson=redisTemplate.opsForValue().get("TOKEN_"+token);
        if (userJson==null){
            return Result.fail(ErrorCode.NO_LOGIN.getCode(),ErrorCode.NO_LOGIN.getMsg());
        }
        SysUser sysUser= JSONObject.parseObject(userJson,SysUser.class);
        LoginUserVo loginUserVo =new LoginUserVo();
        BeanUtils.copyProperties(sysUser, loginUserVo);
        loginUserVo.setAdmin((sysUserMapper.selectById(sysUser.getId()).getAdmin()));
        return Result.success(loginUserVo);
    }

    @Override
    public SysUser FindByAccount(String account) {
        LambdaQueryWrapper<SysUser> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysUser::getAccount,account);
        lambdaQueryWrapper.last("limit 1");
        return sysUserMapper.selectOne(lambdaQueryWrapper);
    }

    @Override
    public void save(SysUser sysUser) {
        sysUserMapper.insert(sysUser);
    }

    @Override
    public UserVo FindById(Long authorId) {
        UserVo userVo=new UserVo();
        SysUser sysUser= sysUserMapper.selectById(authorId);
        BeanUtils.copyProperties(sysUser,userVo);
        return userVo;
    }
}
