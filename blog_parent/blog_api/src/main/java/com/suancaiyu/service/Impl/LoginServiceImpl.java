package com.suancaiyu.service.Impl;

import com.alibaba.fastjson.JSON;
import com.suancaiyu.pojo.SysUser;
import com.suancaiyu.resultcommon.Result;
import com.suancaiyu.service.LoginService;
import com.suancaiyu.service.SysUserService;
import com.suancaiyu.utils.JwtUtils;
import com.suancaiyu.vo.ErrorCode;
import com.suancaiyu.vo.LoginParams;
import com.suancaiyu.vo.LoginUserVo;
import com.suancaiyu.vo.RegisterParams;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class LoginServiceImpl implements LoginService {
    private static final String slat = "mszlu!@###";
    @Resource
    private SysUserService sysUserService;
    @Resource
    private RedisTemplate<String,String> redisTemplate;
    /**
     * 1 检查参数是否为空
     * 2 查询用户表
     * 3 成功返回token 失败返回错误信息
     * 4 将token 与用户信息储存在redis中并且设置过期时间(之后请求都会在redis在验证一遍。前端本地储存token未过期无需再次登入，因为传入后端会进行验证)
     * 5 每次登入
     * @param loginParams
     * @return
     */
    @Override
    public Result login(LoginParams loginParams) {

        String account=loginParams.getAccount();
        String password=loginParams.getPassword();
        if(StringUtils.isBlank(account)||StringUtils.isBlank(password)){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),"用户名密码为空");
        }
        String pwd= DigestUtils.md5Hex(password+slat);//阿帕奇包md5加密
        SysUser user= sysUserService.FindUser(account,pwd);
        if (user==null){
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(), ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }
        String token= JwtUtils.createToken(user.getId());
        redisTemplate.opsForValue().set("TOKEN_"+token, JSON.toJSONString(user),1, TimeUnit.DAYS);//将token存入redis中，将user对象转为json字符串在进行存入
        return Result.success(token);
    }
    @Override
    public Result logout(String token) {
        redisTemplate.delete("TOKEN_"+token);
        return Result.success(null);
    }
    @Override
    public Result register(RegisterParams registerParams) {
        /**
         * 1 判断账号是否已经存在
         * 2 存在返回错误信息
         * 3 不存在，完善信息加密密码，然后插入表中
         *4 注册成功返回token并且存入redis中
         **/
        String account=registerParams.getAccount();
        String password=registerParams.getPassword();
        String nickname=registerParams.getNickname();
        if (StringUtils.isBlank(account)||StringUtils.isBlank(password)||StringUtils.isBlank(nickname)){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
        SysUser sysUser= sysUserService.FindByAccount(account);
        if (sysUser!=null){
            return Result.fail(ErrorCode.USER_EXIST.getCode(), ErrorCode.USER_EXIST.getMsg());
        }
        sysUser=new SysUser();
        sysUser.setNickname(nickname);
        sysUser.setAccount(account);
        sysUser.setPassword(DigestUtils.md5Hex(password+slat));
        sysUser.setAvatar("/static/img/Default.png");//设置默认头像
        sysUser.setCreateDate(System.currentTimeMillis());
        sysUser.setLastLogin(System.currentTimeMillis());
        sysUser.setAdmin(0); //1 为true
        sysUser.setDeleted(0); // 0 为false
        sysUser.setSalt("");
        sysUser.setStatus("");
        sysUser.setEmail("");
        sysUserService.save(sysUser);
        LoginUserVo loginUserVo=new LoginUserVo();
        BeanUtils.copyProperties(sysUser,loginUserVo);
        String token=JwtUtils.createToken(sysUser.getId());
        redisTemplate.opsForValue().set("TOKEN_"+token,JSON.toJSONString(loginUserVo),1,TimeUnit.DAYS);
        return Result.success(token);
    }
}
