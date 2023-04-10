package com.thirteen.smp.controller;

import com.thirteen.smp.response.ResponseBean;
import com.thirteen.smp.pojo.User;
import com.thirteen.smp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 顾建平
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/user") // 配置大类路径
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/get/{userName}") // 配置方法路径
    public ResponseBean getUser(@PathVariable("userName")String userName) {
        User resultUser = userService.getUserByUsername(userName);
        if (resultUser != null) {
            return new ResponseBean(1,"success",resultUser);
        }else{
            return new ResponseBean(0,"查找用户不存在！",null);
        }

    }
}
