package com.thirteen.smp.service.impl;

import com.thirteen.smp.exception.UserAlreadyExistsException;
import com.thirteen.smp.exception.UserNotExistsException;
import com.thirteen.smp.mapper.UserMapper;
import com.thirteen.smp.pojo.User;
import com.thirteen.smp.service.AuthService;
import com.thirteen.smp.utils.SettingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserMapper userMapper;// 使用Spring自动注入工具类

    @Override
    @Transactional // 启用事务
    public User register(User user) throws UserAlreadyExistsException {
        // 检查用户名是否存在
        User target = userMapper.selectByUsername(user.getUsername());
        if (target != null) {
            throw new UserAlreadyExistsException("用户名已存在！");
        }

        // 添加用户属性的默认值
        user.setProfilePic(SettingUtil.getValue("defaultProfilePic"));
        user.setCoverPic(SettingUtil.getValue("defaultCoverPic"));
        user.setUserLang(SettingUtil.getValue("defaultUserLang"));
        user.setUserLocation(SettingUtil.getValue("defaultUserLocation"));

        int count = userMapper.insertUser(user);
        if (count == 1) {
            return user;
        }
        return null;
    }

    @Override
    @Transactional
    public User login(User user) throws UserNotExistsException {
        User target = userMapper.selectByUsername(user.getUsername());
        if (target == null) { // 用户不存在
            throw new UserNotExistsException("用户不存在！");
        }

        if (user.getPassword().equals(target.getPassword())) {
            Map<String, Object> status = userMapper.selectUserStatus(target.getUserId());
            if (status != null) {
                userMapper.updateUserStatus(target.getUserId(), 1);
            } else {
                userMapper.insertUserStatus(target.getUserId(), 1);
            }
            return target;
        }
        return null;// 密码错误
    }

    @Override
    @Transactional
    public boolean logout(Integer userId) {
        Map<String, Object> status = userMapper.selectUserStatus(userId);
        if (status != null) {
            userMapper.updateUserStatus(userId, 0);
        } else {
            userMapper.insertUserStatus(userId, 0);
        }
        return true;
    }
}
