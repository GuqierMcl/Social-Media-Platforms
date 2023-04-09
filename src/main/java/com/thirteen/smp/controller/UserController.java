package com.thirteen.smp.controller;

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

    @RequestMapping("/get/{userName}") // 配置方法路径
    public void getUser(@PathVariable("userName")String userName) {
        // TODO 此处代码还是测试代码，需要完成具体响应操作
        System.out.println(userName + " success!");
    }
}
