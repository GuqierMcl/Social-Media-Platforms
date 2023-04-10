package com.thirteen.smp.mapper;

import com.thirteen.smp.pojo.User;

/**
 * 用于User类数据库处理的Mapper
 * @author 顾建平
 * @version 1.0
 * @since 1.0
 */
public interface UserMapper {

    /**
     * 通过username获取用户对象
     * @param username 用户名
     * @return User对象
     */
    User selectByUsername(String username);

    // TODO 还需定义更多数据库操作
}
