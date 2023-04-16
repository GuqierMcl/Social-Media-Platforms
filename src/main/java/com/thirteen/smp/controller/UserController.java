package com.thirteen.smp.controller;

import com.thirteen.smp.response.ResponseData;
import com.thirteen.smp.pojo.User;
import com.thirteen.smp.service.UserService;
import com.thirteen.smp.utils.ResponseUtil;
import com.thirteen.smp.utils.accessTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 用户操作控制器
 *
 * @author 顾建平
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/user") // 配置类路径
public class UserController {

    @Autowired
    private UserService userService;


    @RequestMapping(path = "/get/{userName}", method = RequestMethod.GET) // 配置方法路径
    public ResponseData getUserByUsername(@PathVariable("userName") String userName) {
        User resultUser = null;
        resultUser = userService.getUserByUsername(userName);
        if (resultUser != null) {
            return ResponseUtil.getSuccessRes(resultUser);
        } else {
            return ResponseUtil.getResponseData(401);
        }
    }

    @RequestMapping(path = "/get-id/{userId}", method = RequestMethod.GET) // 配置方法路径
    public ResponseData getUserByUserId(@PathVariable("userId") Integer userId) {
        User resultUser = null;
        resultUser = userService.getUserByUserId(userId);
        if (resultUser != null) {
            return ResponseUtil.getSuccessRes(resultUser);
        } else {
            return ResponseUtil.getResponseData(401);
        }
    }

    @RequestMapping(path = "/get-all", method = RequestMethod.GET)
    public ResponseData getUserAll() {
        List<User> resultUsers = null;
        resultUsers = userService.getUserAll();
        if (resultUsers != null) {
            return ResponseUtil.getSuccessRes(resultUsers);
        } else {
            return ResponseUtil.getResponseData(0);
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseData updateUserInfo(HttpServletRequest request, @RequestBody User user) {
        String accessToken = accessTokenUtil.getAccessToken(request);
        user.setUserId(accessTokenUtil.getUserId(accessToken));
        user.setUsername(accessTokenUtil.getUsername(accessToken));
        user.setPassword(accessTokenUtil.getPassword(accessToken));

        boolean res = userService.updateUser(user);
        if (res) {
            return ResponseUtil.getSuccessRes(null);
        } else {
            return ResponseUtil.getErrorRes(0);
        }
    }

}