package com.thirteen.smp.service;

import com.thirteen.smp.pojo.User;

import java.util.List;

/**
 * 用户模块业务接口
 * @author 顾建平
 * @version 1.0
 * @since 1.0
 */
public interface UserService {

    /**
     * 通过用户名获取用户信息
     * @param username 用户名
     * @return 用户对象
     */
    User getUserByUsername(String username);

    /**
     * 通过用户ID获取用户信息
     * @param userId 用户ID
     * @return 用户对象
     */
    User getUserByUserId(Integer userId);

    /**
     * 获取所有用户信息
     * @return 用户对象列表
     */
    List<User> getUserAll();

    /**
     * 更新用户信息
     *
     * @param user 用户对象
     * @return 更新结果
     */
    boolean updateUser(User user);


}
