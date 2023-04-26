package com.thirteen.smp.service.impl;

import com.thirteen.smp.exception.UserNotExistsException;
import com.thirteen.smp.mapper.FollowMapper;
import com.thirteen.smp.mapper.UserMapper;
import com.thirteen.smp.pojo.User;
import com.thirteen.smp.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;

@Service
public class FollowServiceImpl implements FollowService {

    @Autowired
    private FollowMapper followMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<Map<String, Object>> getFansInfo(Integer userId) {
        List<Map<String, Object>> res = new ArrayList<>();
        List<User> fans = followMapper.selectByFollowedUserId(userId);

        for (User fan : fans) {
            Map<String, Object> fanMap = new LinkedHashMap<>();

            fanMap.put("userId", fan.getUserId());
            fanMap.put("nickname", fan.getNickname());
            fanMap.put("profilePic", fan.getProfilePic());
            fanMap.put("userLang", fan.getUserLang());
            fanMap.put("userLocation", fan.getUserLocation());

            List<Map<String, Object>> maps = followMapper.selectByUserId(userId, fan.getUserId());
            if (maps.size() == 0) {
                fanMap.put("isFollowing", false);
            } else {
                fanMap.put("isFollowing", true);
            }

            res.add(fanMap);
        }

        return res;
    }

    @Override
    public List<Map<String, Object>> getFollowedUser(Integer userId) {
        List<Map<String, Object>> res = new ArrayList<>();
        List<User> users = followMapper.selectByFollowerUserId(userId);

        for (User user : users) {
            Map<String, Object> fanMap = new LinkedHashMap<>();

            fanMap.put("userId", user.getUserId());
            fanMap.put("nickname", user.getNickname());
            fanMap.put("profilePic", user.getProfilePic());
            fanMap.put("userLang", user.getUserLang());
            fanMap.put("userLocation", user.getUserLocation());

            res.add(fanMap);
        }

        return res;
    }

    @Override
    @Transactional
    public boolean follow(Integer currentUserId, Integer targetUserId) {
        User targetUser = userMapper.selectById(targetUserId);
        if (targetUser == null) {
            throw new UserNotExistsException("用户不存在");
        }

        int res = followMapper.insertFollow(currentUserId, targetUserId, new Timestamp(new Date().getTime()));

        return res == 1;
    }

    @Override
    @Transactional
    public boolean cancelFollow(Integer currentUserId, Integer targetUserId) {
        User targetUser = userMapper.selectById(targetUserId);
        if (targetUser == null) {
            throw new UserNotExistsException("用户不存在");
        }

        int res = followMapper.deleteFollow(currentUserId, targetUserId);

        return res == 1;
    }
}
