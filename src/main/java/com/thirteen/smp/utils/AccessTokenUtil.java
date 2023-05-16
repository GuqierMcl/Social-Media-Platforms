package com.thirteen.smp.utils;

import com.thirteen.smp.pojo.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * 实现对accessToken生成和解析的工具类
 *
 * @author 庄可欣
 */
public class AccessTokenUtil {

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
        Map<String, String> tokenPayloadMap = JwtUtil.getTokenPayloadMap(accessToken);
        return Integer.parseInt(tokenPayloadMap.get("userId"));
    }

    /**
     * 通过accessToken获取用户名
     *
     * @param accessToken accessToken
     * @return 用户名
     */
    public static String getUsername(String accessToken) {
        Map<String, String> tokenPayloadMap = JwtUtil.getTokenPayloadMap(accessToken);
        return tokenPayloadMap.get("username");
    }

    /**
     * 通过accessToken获取密码
     *
     * @param accessToken accessToken
     * @return 密码
     */
    public static String getPassword(String accessToken) {
        Map<String, String> tokenPayloadMap = JwtUtil.getTokenPayloadMap(accessToken);
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
     *
     * @param user 用户对象
     * @return accessToken
     */
    public static String generateAccessToken(User user) {
        return generateAccessToken(user.getUserId(), user.getUsername(), user.getPassword());
    }

    /**
     * 生成accessToken
     *
     * @param userId   用户ID
     * @param username 用户名
     * @param password 密码
     * @return accessToken
     */
    public static String generateAccessToken(Integer userId, String username, String password) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", String.valueOf(userId));
        map.put("username", username);
        map.put("password", password);
        return JwtUtil.generateToken(map);
    }

    /**
     * 将accessToken保存到cookies中
     *
     * @param accessToken accessToken
     * @param response    响应对象
     * @return 保存结果
     */
    public static void bindTokenToCookies(String accessToken, HttpServletResponse response) {
        Cookie cookie = new Cookie("accessToken", accessToken);
        cookie.setMaxAge(60 * 60 * 24 * 30);
        cookie.setPath("/");
        System.out.println("bindToCookies");
        response.addCookie(cookie);
    }

    /**
     * 将accessToken从cookies中去除（退出登录）
     * @param response    响应对象
     * @return 保存结果
     */
    public static void removeTokenToCookies(HttpServletResponse response) {
        Cookie cookie = new Cookie("accessToken",null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        System.out.println("removeToCookies");
        response.addCookie(cookie);
    }
}
