package com.thirteen.smp.service;

import com.thirteen.smp.exception.UserAlreadyExistsException;
import com.thirteen.smp.exception.UserNotExistsException;
import com.thirteen.smp.pojo.User;

/**
 * 权限验证模块业务接口
 * @author 顾建平
 */
public interface AuthService {

    User register(User user) throws UserAlreadyExistsException;

    User login(User user) throws UserNotExistsException;

    boolean logout();

}
