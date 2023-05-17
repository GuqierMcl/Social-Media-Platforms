package com.thirteen.smp.controller;

import com.thirteen.smp.exception.UserAlreadyExistsException;
import com.thirteen.smp.exception.UserNotExistsException;
import com.thirteen.smp.pojo.User;
import com.thirteen.smp.response.ResponseData;
import com.thirteen.smp.service.AuthService;
import com.thirteen.smp.utils.AccessTokenUtil;
import com.thirteen.smp.utils.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 权限验证控制器
 *
 * @author 庄可欣
 * @version 1.0
 * @since 1.0
 */

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;


    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public ResponseData login(HttpServletResponse response, @RequestBody User user) {
        User resultUser = null;
        try {
            resultUser = authService.login(user);
        } catch (UserNotExistsException e) {
            return ResponseUtil.getErrorRes(401); // 用户不存在
        }
        if (resultUser == null) {
            return ResponseUtil.getErrorRes(403); // 密码错误
        }

        // 为当前用户配置accessToken
        String accessToken = AccessTokenUtil.generateAccessToken(resultUser);
        AccessTokenUtil.bindTokenToCookies(accessToken, response);

        return ResponseUtil.getSuccessRes(resultUser);
    }

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public ResponseData register(@RequestBody User user) {
        User resultUser = null;

        try {
            resultUser = authService.register(user);
        } catch (UserAlreadyExistsException e) {
            return ResponseUtil.getErrorRes(402); //用户名已存在
        }

        if (resultUser == null) {
            return ResponseUtil.getErrorRes(501); //更新数据库失败
        }

        return ResponseUtil.getSuccessRes(resultUser);
    }

    @RequestMapping(path = "/logout", method = RequestMethod.POST)
    public ResponseData logout(HttpServletResponse response, HttpServletRequest request) {
        AccessTokenUtil.removeTokenToCookies(response); //收回AccessToken
        authService.logout(AccessTokenUtil.getUserId(request));
        return ResponseUtil.getSuccessRes(null);
    }

}
