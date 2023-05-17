package com.thirteen.smp.service.impl;

import com.thirteen.smp.mapper.FollowMapper;
import com.thirteen.smp.mapper.PostMapper;
import com.thirteen.smp.mapper.UserMapper;
import com.thirteen.smp.pojo.Post;
import com.thirteen.smp.pojo.User;
import com.thirteen.smp.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class HomeServiceImpl implements HomeService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private FollowMapper followMapper;

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

    @Override
    public List<Map<String, Object>> getPostUpdate(Integer count, Integer userId) {
        /*
        获取关注用户列表
        获取关注用户最新post
         */
        List<Map<String, Object>> resultList = new LinkedList<>();
        // 获取关注用户
        List<User> users = followMapper.selectByFollowerUserId(userId);
        // 获取关注用户的帖子
        List<Post> postList = new ArrayList<>();
        for (User user : users) {
            List<Post> posts = postMapper.selectByUserId(user.getUserId());
            postList.addAll(posts);
        }
        postList.sort(Comparator.comparing(Post::getPostTime)); // 按照发布时间排序
        Collections.reverse(postList);// 逆序
        int cnt = 0;
        for (Post post : postList) {
            if(cnt == count) break;
            User user = userMapper.selectById(post.getUserId());
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("userId",user.getUserId());
            item.put("username",user.getUsername());
            item.put("nickname",user.getNickname());
            item.put("content",post.getContent());
            item.put("time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(post.getPostTime()));
            resultList.add(item);
            cnt ++;
        }
        return resultList;
    }

    @Override
    public List<Map<String, Object>> getOnlineFriend(Integer count, Integer userId) {
        return null;
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
