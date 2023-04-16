package com.thirteen.smp.mapper;

import com.thirteen.smp.pojo.User;

import java.util.List;

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
     * @return 用户对象
     */
    User selectByUsername(String username);

    /**
     * 通过id获取用户对象
     * @param id 用户id
     * @return 用户对象
     */
    User selectById(Integer id);

    /**
     * 查询所有用户信息
     * @return 所用用户对象列表
     */
    List<User> selectAll();

    /**
     * 新增用户信息
     * @param user 用户对象
     * @return 插入修改数据条数
     */
    int insertUser(User user);

    /**
     * 通过用户ID删除用户
     * @param id 用户ID
     * @return 删除数据条数
     */
    int deleteUserById(Integer id);

    /**
     * 更新用户信息
     * @param user 用户对象
     * @return 更新记录条数
     */
    int updateUser(User user);
}
