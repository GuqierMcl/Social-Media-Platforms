package com.thirteen.smp.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thirteen.smp.mapper.*;
import com.thirteen.smp.pojo.Comment;
import com.thirteen.smp.pojo.Favorite;
import com.thirteen.smp.pojo.Post;
import com.thirteen.smp.pojo.User;
import com.thirteen.smp.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatisticsServicelmpl implements StatisticsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private LikeMapper likeMapper;

    @Autowired
    private FollowMapper followMapper;

    @Autowired
    private FavoriteMapper favoriteMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public Map<String, Object> getUserStatistics() {
        Map<String, Object> userSatistics = new LinkedHashMap<>();
        userSatistics.put("registerNum", userMapper.selectAll().size());
        userSatistics.put("onlineNum", userMapper.getOnlineUserId().size());
        return userSatistics;
    }

    @Override
    public Map<String, Object> getPostNum() {
        Map<String, Object> result = new LinkedHashMap<>();
        List<Post> posts = postMapper.selectAllPost();
        List<Post> postList = new ArrayList<>();
        Timestamp lastWeek = new Timestamp(System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000);
        posts.forEach(post -> {
            if (post.getPostTime().compareTo(lastWeek) > 0) {
                postList.add(post);
            }
        });
        result.put("postNum", postList.size());
        return result;
    }

    @Override
    public List<Map<String, Object>> getHotUserList(Integer count) throws Exception {
        /*
         * 用户热度 = 粉丝数 * 0.3 + 发帖量 * 0.3 + 点赞数 * 0.15 + 收藏量 * 0.1 + 评论数 * 0.1 + 关注量 * 0.05
         */
        List<User> users = userMapper.selectAll();

        // 将用户对象列表转为Map对象
        ObjectMapper objectMapper = new ObjectMapper();
        String s = objectMapper.writeValueAsString(users);
        List<Map<String, Object>> userMapList = objectMapper.readValue(s, new TypeReference<List<Map<String, Object>>>() {
        });

        userMapList.sort((user1, user2) -> {
            // 获取粉丝数
            List<User> fans1 = followMapper.selectByFollowedUserId((Integer) user1.get("userId"));
            List<User> fans2 = followMapper.selectByFollowedUserId((Integer) user2.get("userId"));
            user1.put("fansNum", fans1.size());
            user2.put("fansNum", fans2.size());
            // 获取发帖量
            List<Post> posts1 = postMapper.selectByUserId((Integer) user1.get("userId"));
            List<Post> posts2 = postMapper.selectByUserId((Integer) user2.get("userId"));
            user1.put("PostNum", posts1.size());
            user2.put("PostNum", posts2.size());
            // 获取点赞数
            List<Comment> comments1 = likeMapper.selectLikeCommentByUserId((Integer) user1.get("userId"));
            List<Comment> comments2 = likeMapper.selectLikeCommentByUserId((Integer) user2.get("userId"));
            List<Post> likePosts1 = likeMapper.selectLikePostByUserId((Integer) user1.get("userId"));
            List<Post> likePosts2 = likeMapper.selectLikePostByUserId((Integer) user2.get("userId"));
            user1.put("commentLikeNum", comments1.size());
            user2.put("commentLikeNum", comments2.size());
            user1.put("postLikeNum", likePosts1.size());
            user2.put("postLikeNum", likePosts2.size());
            // 获取收藏量
            List<Favorite> favorites1 = favoriteMapper.selectByUserId((Integer) user1.get("userId"));
            List<Favorite> favorites2 = favoriteMapper.selectByUserId((Integer) user2.get("userId"));
            user1.put("favoriteNum", favorites1.size());
            user2.put("favoriteNum", favorites2.size());
            // 获取评论量
            List<Comment> comments11 = commentMapper.selectByUserId((Integer) user1.get("userId"));
            List<Comment> comments22 = commentMapper.selectByUserId((Integer) user2.get("userId"));
            user1.put("commentNum", comments11.size());
            user2.put("commentNum", comments22.size());
            // 获取关注量
            List<User> followUsers1 = followMapper.selectByFollowerUserId((Integer) user1.get("userId"));
            List<User> followUsers2 = followMapper.selectByFollowerUserId((Integer) user2.get("userId"));
            user1.put("followNum", followUsers1.size());
            user2.put("followNum", followUsers2.size());

            // 根据排序规则进行排序
            double hotness1 = fans1.size() * 0.3 + posts1.size() * 0.3
                    + (comments1.size() + likePosts1.size()) * 0.15 + favorites1.size() * 0.1
                    + comments11.size() * 0.1 + followUsers1.size() * 0.05;
            double hotness2 = fans2.size() * 0.3 + posts2.size() * 0.3
                    + (comments2.size() + likePosts2.size()) * 0.15 + favorites2.size() * 0.1
                    + comments22.size() * 0.1 + followUsers2.size() * 0.05;
            user1.put("hotness", hotness1);
            user2.put("hotness", hotness2);
            return Double.compare(hotness2, hotness1);
        });
        return userMapList.subList(0, count <= userMapList.size() ? count : userMapList.size());
    }

    @Override
    public List<Post> getHotPostList() {
        return null;
    }
}
