package com.thirteen.smp.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.thirteen.smp.mapper.FollowMapper;
import com.thirteen.smp.mapper.UserMapper;
import com.thirteen.smp.pojo.User;
import com.thirteen.smp.service.UserService;
import com.thirteen.smp.utils.ProvinceMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper; // 使用Spring自动注入工具类

    @Autowired
    private FollowMapper followMapper; // 使用Spring自动注入工具类

    @Override
    public User getUserByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    @Override
    public User getUserByUserId(Integer userId) {
        return userMapper.selectById(userId);
    }

    @Override
    public Map<String, Object> getUserByUserIdPlusFollow(Integer userId, Integer targetUserId) throws JsonProcessingException {
        User targetUser = userMapper.selectById(targetUserId);
        if (targetUser == null) return null;

        ObjectMapper objectMapper = new ObjectMapper();

        // 将pojo对象转换为Map
        String s = objectMapper.writeValueAsString(targetUser);
        Map<String, Object> map = objectMapper.readValue(s, new TypeReference<Map<String, Object>>() {
        });

        // 判断是否关注
        Map<String, Object> followMap = followMapper.selectByUserId(userId, targetUserId);
        if (followMap != null) {
            map.put("isFollowed", true);
        } else {
            map.put("isFollowed", false);
        }
        return map;
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

        // 将接收到的省份拼音映射为省份名称
        user.setUserLocation(ProvinceMapperUtil.toName(user.getUserLocation()) != null ? ProvinceMapperUtil.toName(user.getUserLocation()) : user.getUserLocation());
        int count = userMapper.updateUser(user);
        return count == 1;
    }

    @Override
    @Transactional
    public boolean markOnline(Integer userId) {
        Map<String, Object> status = userMapper.selectUserStatus(userId);
        if (status != null) {
            userMapper.updateUserStatus(userId, 1);
        } else {
            userMapper.insertUserStatus(userId, 1);
        }
        return true;
    }

    @Override
    @Transactional
    public boolean markOffline(Integer userId) {
        Map<String, Object> status = userMapper.selectUserStatus(userId);
        if (status != null) {
            userMapper.updateUserStatus(userId, 0);
        } else {
            userMapper.insertUserStatus(userId, 0);
        }
        return true;
    }

}
