package com.thirteen.smp.service.impl;

import com.thirteen.smp.mapper.FavoriteMapper;
import com.thirteen.smp.mapper.FollowMapper;
import com.thirteen.smp.mapper.PostMapper;
import com.thirteen.smp.mapper.UserMapper;
import com.thirteen.smp.pojo.Favorite;
import com.thirteen.smp.pojo.Post;
import com.thirteen.smp.pojo.User;
import com.thirteen.smp.service.FollowService;
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

    @Autowired
    private FollowService followService;

    @Autowired
    private FavoriteMapper favoriteMapper;

    @Override
    public List<Map<String, Object>> getRecommendUser(Integer count, Integer userId) {
        /*
        获取所有用户列表
        查找收藏夹中的作者用户
        查找地区相同的用户
        查找语言相同的用户
        算法匹配剩余用户
         */
        List<Map<String, Object>> resultList = new ArrayList<>();

        User crrUser = userMapper.selectById(userId); // 获取当前用户信息
        List<User> users = userMapper.selectAll(); // 获取所有用户信息
        List<User> usersBak = userMapper.selectAll();
        Collections.shuffle(users); // 打乱列表顺序

        // 查找收藏夹中的作者用户
        List<Favorite> favorites = favoriteMapper.selectByUserId(userId);
        for (Favorite favorite : favorites) {
            Integer postId = favorite.getPostId();
            Integer authorUserId = postMapper.selectByPostId(postId).getUserId();
            for (User user : users) {
                if (resultList.size() == count) break;
                if (user.getUserId().equals(authorUserId)){
                    if (user.getUserId().equals(userId)) continue;
                    userListHandler(resultList, user, userId, "收藏过TA的帖子");
                    usersBak.remove(user);
                }
            }
        }

        // 查找地区相同或语言相同的用户
        for (User user : users) {
            if (resultList.size() == count) break;
            if (user.getUserId().equals(userId)) continue;
            if (user.getUserLocation().equals(crrUser.getUserLocation()) || user.getUserLang().equals(crrUser.getUserLang())) {
                userListHandler(resultList, user, userId, "与TA地区/语言相同");
                usersBak.remove(user);
                System.out.println(resultList.size());
            }
        }
        Collections.shuffle(usersBak);
        // 查找剩余用户
        if (resultList.size() < count) {
            for (User user : usersBak) {
                if (resultList.size() == count) break;
                if (user.getUserId().equals(userId)) continue;
                userListHandler(resultList, user, userId, "推荐关注");
            }
        }
        Collections.shuffle(resultList);
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
            if (cnt == count) break;
            User user = userMapper.selectById(post.getUserId());
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("userId", user.getUserId());
            item.put("username", user.getUsername());
            item.put("nickname", user.getNickname());
            item.put("profilePic", user.getProfilePic());
            item.put("content", post.getContent());
            item.put("time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(post.getPostTime()));
            resultList.add(item);
            cnt++;
        }
        return resultList;
    }

    @Override
    public List<Map<String, Object>> getOnlineFriend(Integer count, Integer userId) {
        /*
        获取关注用户
        判断是否互关（为好友）
        判断是否在线
         */
        List<Map<String, Object>> resultList = new LinkedList<>();
        // 获取关注用户
        List<User> beFollowedUsers = followMapper.selectByFollowerUserId(userId);
        for (User user : beFollowedUsers) {
            // 判断是否互关
            boolean each = followService.isFollowEach(userId, user.getUserId());
            // 判断是否在线
            Map<String, Object> map = userMapper.selectUserStatus(user.getUserId());
            boolean online;
            if (map == null) {
                online = false;
            }else {
                online = (Long) map.get("status")== 1;
            }

            if (each && online) {
                Map<String, Object> item = new LinkedHashMap<>();
                item.put("userId", user.getUserId());
                item.put("username", user.getUsername());
                item.put("nickname", user.getNickname());
                item.put("profilePic", user.getProfilePic());
                item.put("language", user.getUserLang());
                item.put("location", user.getUserLocation());
                resultList.add(item);
            }
        }
        return resultList;
    }

    /**
     * 私有方法，将用户信息封装到List中，提高代码复用
     *
     * @param resultList 结果列表
     * @param user       用户对象
     */
    private void userListHandler(List<Map<String, Object>> resultList, User user, Integer userId, String reason) {
        if (user.getUserId().equals(userId)) return;
        Map<String, Object> item = new LinkedHashMap<>();
        item.put("userId", user.getUserId());
        item.put("username", user.getUsername());
        item.put("nickname", user.getNickname());
        item.put("profilePic", user.getProfilePic());
        item.put("language", user.getUserLang());
        item.put("location", user.getUserLocation());
        Map<String, Object> map = followMapper.selectByUserId(userId, user.getUserId());
        item.put("isFollowed", map != null);
        item.put("reason", reason);
        resultList.add(item);
    }

}
