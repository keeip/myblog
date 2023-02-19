package com.suancaiyu.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {
    private static final String secret="123456Mszlu!@###$$";//密钥
    public static String createToken(Long Id){
        Map<String,Object> claims=new HashMap<>();//设置自定义内容
        claims.put("userId",Id);
        JwtBuilder jwtBuilder= Jwts.builder()
                .signWith(SignatureAlgorithm.HS256,secret)//签订算法与密钥
                .setClaims(claims)
                .setIssuedAt(new Date())//设置签发时间
                .setExpiration(new Date(System.currentTimeMillis()+24 * 60 * 60 * 60 * 1000));//设置过期时间
        String token=jwtBuilder.compact();
        return token;
    }

    /**
     * 通过获取token自定义内容来判断token是否有效
     * @param token
     * @return
     */
    public static Map<String,Object> checkToken(String token){
        try{
            Map<String,Object> claims= (Map<String, Object>) Jwts.parser().setSigningKey(secret).parse(token).getBody();
            return claims;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
}
