package com.thirteen.smp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.thirteen.smp.response.ResponseData;
import com.thirteen.smp.pojo.User;
import com.thirteen.smp.service.UserService;
import com.thirteen.smp.utils.ResponseUtil;
import com.thirteen.smp.utils.AccessTokenUtil;
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
    public ResponseData getUserByUserId(@PathVariable("userId") Integer targetUserId, HttpServletRequest request) {
        /*
        User resultUser = null;
        resultUser = userService.getUserByUserId(userId);
        if (resultUser != null) {
            return ResponseUtil.getSuccessRes(resultUser);
        } else {
            return ResponseUtil.getResponseData(401);
        }
         */
        Integer crrUserId = AccessTokenUtil.getUserId(request);
        Map<String, Object> userByUserIdPlusFollow = null;
        try {
            userByUserIdPlusFollow = userService.getUserByUserIdPlusFollow(crrUserId, targetUserId);
        } catch (JsonProcessingException e) {
            return ResponseUtil.getErrorRes(501);
        }
        return ResponseUtil.getSuccessRes(userByUserIdPlusFollow);
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
        String accessToken = AccessTokenUtil.getAccessToken(request);
        user.setUserId(AccessTokenUtil.getUserId(accessToken));
        user.setUsername(AccessTokenUtil.getUsername(accessToken));
        user.setPassword(AccessTokenUtil.getPassword(accessToken));

        boolean res = userService.updateUser(user);
        if (res) {
            return ResponseUtil.getSuccessRes(null);
        } else {
            return ResponseUtil.getErrorRes(0);
        }
    }

    @RequestMapping(value = "/markline",method = RequestMethod.POST)
    public Object markOnline(HttpServletRequest request){
        Integer userId = AccessTokenUtil.getUserId(request);
        return ResponseUtil.getSuccessRes(userService.markOnline(userId));
    }

    @RequestMapping(value = "/markline",method = RequestMethod.DELETE)
    public Object markOffline(HttpServletRequest request){
        Integer userId = AccessTokenUtil.getUserId(request);
        return ResponseUtil.getSuccessRes(userService.markOffline(userId));
    }

}
