package com.thirteen.smp.utils;

import com.thirteen.smp.pojo.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * 实现对accessToken生成和解析的工具类
 *
 * @author 顾建平
 */
public class accessTokenUtil {

    /**
     * 从request的cookies中获取accessToken
     *
     * @param request 请求对象
     * @return accessToken
     */
    public static String getAccessToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String accessToken = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("accessToken")) {
                    accessToken = cookie.getValue();
                }
            }
        }

        return accessToken;
    }

    /**
     * 通过accessToken获取用户ID
     *
     * @param accessToken accessToken
     * @return 用户ID
     */
    public static Integer getUserId(String accessToken) {
        Map<String, String> tokenPayloadMap = jwtUtil.getTokenPayloadMap(accessToken);
        return Integer.parseInt(tokenPayloadMap.get("userId"));
    }

    /**
     * 通过accessToken获取用户名
     *
     * @param accessToken accessToken
     * @return 用户名
     */
    public static String getUsername(String accessToken) {
        Map<String, String> tokenPayloadMap = jwtUtil.getTokenPayloadMap(accessToken);
        return tokenPayloadMap.get("username");
    }

    /**
     * 通过accessToken获取密码
     *
     * @param accessToken accessToken
     * @return 密码
     */
    public static String getPassword(String accessToken) {
        Map<String, String> tokenPayloadMap = jwtUtil.getTokenPayloadMap(accessToken);
        return tokenPayloadMap.get("password");
    }

    /**
     * 从accessToken中获取用户ID
     *
     * @param request 请求对象
     * @return 用户ID
     */
    public static Integer getUserId(HttpServletRequest request) {
        String accessToken = getAccessToken(request);
        return getUserId(accessToken);
    }

    /**
     * 从accessToken中获取用户名
     *
     * @param request 请求对象
     * @return 用户名
     */
    public static String getUsername(HttpServletRequest request) {
        String accessToken = getAccessToken(request);
        return getUsername(accessToken);
    }

    /**
     * 从accessToken中获取密码
     *
     * @param request 请求对象
     * @return 密码
     */
    public static String getPassword(HttpServletRequest request) {
        String accessToken = getAccessToken(request);
        return getPassword(accessToken);
    }

    /**
     * 生成accessToken
     * @param user 用户对象
     * @return accessToken
     */
    public static String generateAccessToken(User user) {
        return generateAccessToken(user.getUserId(), user.getUsername(), user.getPassword());
    }

    /**
     * 生成accessToken
     * @param userId 用户ID
     * @param username 用户名
     * @param password 密码
     * @return accessToken
     */
    public static String generateAccessToken(Integer userId, String username, String password) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", String.valueOf(userId));
        map.put("username", username);
        map.put("password", password);
        return jwtUtil.generateToken(map);
    }

}
