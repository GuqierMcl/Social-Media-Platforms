package com.thirteen.smp.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT生成token和解析token的工具类
 */
public class JwtUtil {

    /**
     * 密钥
     */
    private static final String SECRET_KEY = SettingUtil.getSecretKey();

    /**
     * 持续时间，即有效期
     */
    public final static long KEEP_TIME = 30L * 24 * 60 * 60 * 1000;

    /**
     * 生成token
     * @param map payload中需要放置的信息
     * @return 返回生成的token
     */
    public static String generateToken(Map<String, String> map) {
        // 获取JWT构造对象
        JWTCreator.Builder builder = JWT.create();
        // 存入数据到payload
        map.forEach((k, v) -> {
            builder.withClaim(k, v);
        });
        String token = builder.withExpiresAt(new Date(System.currentTimeMillis() + KEEP_TIME)) // token有效期
                .sign(Algorithm.HMAC256(SECRET_KEY)); // 签名

        return token;
    }

    /**
     * 验证token DecodedJWT 为解密之后的对象 可以获取payload中添加的数据
     * @param token token字符串
     * @return DecodedJWT为解密之后的对象，可以获取payload中添加的数据
     */
    public static DecodedJWT verifyToken(String token) {
        return JWT.require(Algorithm.HMAC256(SECRET_KEY)).build().verify(token);
    }

    /**
     * 获取token中payload中的添加的参数
     * @param token token字符串
     * @return 结果map集合
     */
    public static Map<String, String> getTokenPayloadMap(String token) {
        DecodedJWT verify = JWT.require(Algorithm.HMAC256(SECRET_KEY)).build().verify(token);
        Map<String, String> payload = new HashMap<>();

        //Map<String, Claim> claims = verify.getClaims(); //获取payload中所有的参数
        //verify.getClaim("userName").asString();  //通过key获取value,转成对应的类型

        Map<String, Claim> claims = verify.getClaims();
        claims.forEach((k, v) -> {
            payload.put(k, v.asString());
        });

        return payload;
    }

}
