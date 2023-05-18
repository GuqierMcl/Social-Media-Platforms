package com.thirteen.smp.mapper;

import com.thirteen.smp.pojo.User;

import java.util.List;
import java.util.Map;

/**
 * 用于t_user表数据库操作的Mapper
 * @version 1.0
 * @since 1.0
 */
public interface UserMapper {

    /**
     * 通过当前在线用户数量
     * @param 无
     * @return 在线用户数量
     */
    int getOnlineNum();

    /**
     * 通过当前注册用户数量
     * @param 无
     * @return 注册用户数量
     */
    int getRegisterNum();

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

    /**
     * 查询用户登录状态
     * @param userId 用户ID
     * @return 用户状态结果Map
     */
    Map<String,Object> selectUserStatus(Integer userId);

    /**
     * 新增用户状态
     * @param userId 用户ID
     * @param status 状态
     * @return 执行结果
     */
    int insertUserStatus(Integer userId, Integer status);

    /**
     * 更新用户状态
     * @param userId 用户ID
     * @param status 状态
     * @return 执行结果
     */
    int updateUserStatus(Integer userId, Integer status);
}
