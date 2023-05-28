package com.thirteen.smp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.thirteen.smp.pojo.User;

import java.util.List;
import java.util.Map;

/**
 * 用户模块业务接口
 * @author 顾建平
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
     * 通过用户ID获取用户包括是否关注的信息
     * @param userId 当前用户ID
     * @param targetUserId 目标用户ID
     * @return 用户信息Map
     */
    Map<String,Object> getUserByUserIdPlusFollow(Integer userId, Integer targetUserId) throws JsonProcessingException;

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

    /**
     * 标记用户上线
     * @param userId 用户ID
     * @return 结果
     */
    boolean markOnline(Integer userId);

    /**
     * 标记用户下线
     * @param userId 用户ID
     * @return 结果
     */
    boolean markOffline(Integer userId);


}
