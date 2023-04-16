package com.thirteen.smp.service.impl;

import com.thirteen.smp.mapper.UserMapper;
import com.thirteen.smp.pojo.User;
import com.thirteen.smp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper; // 使用Spring自动注入工具类

    @Override
    public User getUserByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    @Override
    public User getUserByUserId(Integer userId) {
        return userMapper.selectById(userId);
    }

    @Override
    public List<User> getUserAll() {
        return userMapper.selectAll();
    }

    @Override
    @Transactional // 添加事务管理器注解
    public boolean updateUser(User user) {
        User targetUser = userMapper.selectById(user.getUserId());
        if (targetUser == null) return false;

        int count = userMapper.updateUser(user);
        return count == 1;
    }

}
