package com.thirteen.smp.service;

import com.thirteen.smp.exception.UserAlreadyExistsException;
import com.thirteen.smp.exception.UserNotExistsException;
import com.thirteen.smp.pojo.User;

/**
 * 权限验证模块业务接口
 * @author 庄可欣
 */
public interface AuthService {

    /**
     * 用户注册业务方法
     * @param user 用户对象
     * @return 注册后的用户对象
     * @throws UserAlreadyExistsException 用户已存在异常
     */
    User register(User user) throws UserAlreadyExistsException;

    /**
     * 用户登录业务方法
     * @param user 用户对象
     * @return 登陆后获取的用户信息
     * @throws UserNotExistsException 用户不存在异常
     */
    User login(User user) throws UserNotExistsException;

    /**
     * 用户登出业务方法
     * @param userId 用户ID
     * @return 执行结果
     */
    boolean logout(Integer userId);

}
