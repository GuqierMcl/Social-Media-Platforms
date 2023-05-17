package com.thirteen.smp.service.impl;

import com.thirteen.smp.mapper.UserMapper;
import com.thirteen.smp.pojo.User;
import com.thirteen.smp.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class HomeServiceImpl implements HomeService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<Map<String, Object>> getRecommendUser(Integer count, Integer userId) {
        /*
        获取所有用户列表
        查找地区相同的用户
        查找语言相同的用户
        算法匹配剩余用户
         */
        List<Map<String, Object>> resultList = new ArrayList<>();
        int cnt = 0;

        User crrUser = userMapper.selectById(userId); // 获取当前用户信息
        List<User> users = userMapper.selectAll(); // 获取所有用户信息
        List<User> usersBak = userMapper.selectAll();
        // 查找地区相同和语言相同的用户
        for (User user : users) {
            if (cnt == count) break;
            if (user.getUserLocation().equals(crrUser.getUserLocation()) || user.getUserLang().equals(crrUser.getUserLang())) {
                userListHandler(resultList, user);
                usersBak.remove(user);
                cnt++;
            }
        }
        // 查找剩余用户
        if (cnt < count) {
            for (User user : usersBak) {
                if (cnt == count) break;
                userListHandler(resultList, user);
                cnt++;
            }
        }
        return resultList;
    }

    /**
     * 私有方法，将用户信息封装到List中，提高代码复用
     * @param resultList 结果列表
     * @param user 用户对象
     */
    private void userListHandler(List<Map<String, Object>> resultList, User user) {
        Map<String, Object> item = new LinkedHashMap<>();
        item.put("userId", user.getUserId());
        item.put("username", user.getUsername());
        item.put("nickname", user.getNickname());
        item.put("profilePic", user.getProfilePic());
        resultList.add(item);
    }

}
