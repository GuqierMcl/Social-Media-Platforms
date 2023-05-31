package com.thirteen.smp.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
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
public class StatisticsServiceImpl implements StatisticsService {

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
        Map<String, Object> userStatistics = new LinkedHashMap<>();
        userStatistics.put("registerNum", userMapper.selectAll().size());
        userStatistics.put("onlineNum", userMapper.getOnlineUserId().size());
        return userStatistics;
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
    public List<Map<String, Object>> getHotUserList(Integer count, Integer userId) throws Exception {
        /*
         * 用户热度 = 粉丝数 * 0.3 + 发帖量 * 0.3 + 点赞数 * 0.15 + 收藏量 * 0.1 + 评论数 * 0.1 + 关注量 * 0.05
         */
        List<User> users = userMapper.selectAll();

        // 将用户对象列表转为Map对象
        ObjectMapper objectMapper = new ObjectMapper();
        String s = objectMapper.writeValueAsString(users);
        List<Map<String, Object>> userMapList = objectMapper.readValue(s, new TypeReference<>() {
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
            // 获取被点赞数
            int likeNum1 = 0;
            int likeNum2 = 0;
            List<Comment> comments1 = commentMapper.selectByUserId((Integer) user1.get("userId"));
            List<Comment> comments2 = commentMapper.selectByUserId((Integer) user2.get("userId"));
            for (Comment comment : comments1) {
                likeNum1 += likeMapper.selectCommentLikeNum(comment.getCommentId());
            }
            for (Comment comment : comments2) {
                likeNum2 += likeMapper.selectCommentLikeNum(comment.getCommentId());
            }
            for (Post post : posts1) {
                likeNum1 += post.getLikeNum();
            }
            for (Post post : posts2) {
                likeNum2 += post.getLikeNum();
            }
            user1.put("beLikeNum", likeNum1);
            user2.put("beLikeNum", likeNum2);

            // 获取被收藏量
            int favoriteNum1 = 0;
            int favoriteNum2 = 0;
            for (Post post : posts1) {
                List<Favorite> favorites = favoriteMapper.selectByPostId(post.getPostId());
                favoriteNum1 += favorites.size();
            }
            for (Post post : posts2) {
                List<Favorite> favorites = favoriteMapper.selectByPostId(post.getPostId());
                favoriteNum2 += favorites.size();
            }
            user1.put("beFavoriteNum", favoriteNum1);
            user2.put("beFavoriteNum", favoriteNum2);
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
            // 判断是否关注
            Map<String, Object> map1 = followMapper.selectByUserId(userId, (Integer) user1.get("userId"));
            Map<String, Object> map2 = followMapper.selectByUserId(userId, (Integer) user2.get("userId"));
            if (map1 != null) user1.put("isFollowing", true);
            else user1.put("isFollowing", false);
            if (map2 != null) user2.put("isFollowing", true);
            else user2.put("isFollowing", false);

            // 根据排序规则进行排序
            double hotness1 = fans1.size() * 0.3 + posts1.size() * 0.3
                    + likeNum1 * 0.15 + favoriteNum1 * 0.1
                    + comments11.size() * 0.1 + followUsers1.size() * 0.05;
            double hotness2 = fans2.size() * 0.3 + posts2.size() * 0.3
                    + likeNum2 * 0.15 + favoriteNum2 * 0.1
                    + comments22.size() * 0.1 + followUsers2.size() * 0.05;
            user1.put("hotness", hotness1);
            user2.put("hotness", hotness2);
            return Double.compare(hotness2, hotness1);
        });
        return userMapList.subList(0, count <= userMapList.size() ? count : userMapList.size());
    }

    @Override
    public List<Map<String, Object>> getHotPostList(Integer count) throws Exception {
        /*
         帖子热度 = 点赞数 * 0.3 + 收藏量 * 0.5 + 评论量 * 0.15 + 评论点赞量 * 0.05
         */
        List<Post> posts = postMapper.selectAllPost();

        // 将帖子对象列表转换为Map列表
        ObjectMapper objectMapper = new ObjectMapper();
        String s = objectMapper.writeValueAsString(posts);
        List<Map<String, Object>> postMaps = objectMapper.readValue(s, new TypeReference<>() {
        });

        postMaps.sort((post1, post2) -> {
            // 获取收藏量
            List<Favorite> favorites1 = favoriteMapper.selectByPostId((Integer) post1.get("postId"));
            List<Favorite> favorites2 = favoriteMapper.selectByPostId((Integer) post2.get("postId"));
            post1.put("favoriteNum", favorites1.size());
            post2.put("favoriteNum", favorites2.size());

            // 获取评论数
            int commCnt1 = commentMapper.selectCountByPostId((Integer) post1.get("postId"));
            int commCnt2 = commentMapper.selectCountByPostId((Integer) post2.get("postId"));
            post1.put("commentNum", commCnt1);
            post2.put("commentNum", commCnt2);

            // 获取评论点赞总数
            int comLike1 = 0;
            int comLike2 = 0;
            List<Comment> comments1 = commentMapper.selectByPostId((Integer) post1.get("postId"));
            List<Comment> comments2 = commentMapper.selectByPostId((Integer) post2.get("postId"));
            for (Comment comment : comments1) {
                Integer num = likeMapper.selectCommentLikeNum(comment.getCommentId());
                comLike1 += num;
            }
            for (Comment comment : comments2) {
                Integer num = likeMapper.selectCommentLikeNum(comment.getCommentId());
                comLike2 += num;
            }

            // 完善帖子用户信息
            User user1 = userMapper.selectById((Integer) post1.get("userId"));
            User user2 = userMapper.selectById((Integer) post2.get("userId"));
            post1.put("userId", user1.getUserId());
            post2.put("userId", user2.getUserId());
            post1.put("nickname", user1.getNickname());
            post2.put("nickname", user2.getNickname());
            post1.put("profilePic", user1.getProfilePic());
            post2.put("profilePic", user2.getProfilePic());
            post1.put("isLike", likeMapper.judgeLiked((Integer) post1.get("postId"), user1.getUserId()) != 0);
            post2.put("isLike", likeMapper.judgeLiked((Integer) post2.get("postId"), user1.getUserId()) != 0);

            post1.put("isStaring", favoriteMapper.selectByUserIdAndPostId(
                    new Favorite(null, (Integer) post1.get("postId"), user1.getUserId(), null)) != null);
            post2.put("isStaring", favoriteMapper.selectByUserIdAndPostId(
                    new Favorite(null, (Integer) post2.get("postId"), user2.getUserId(), null)) != null);

            double hotness1 = (Integer) post1.get("likeNum") * 0.3 + favorites1.size() * 0.5
                    + commCnt1 * 0.15 + comLike1 * 0.05;
            double hotness2 = (Integer) post2.get("likeNum") * 0.3 + favorites2.size() * 0.5
                    + commCnt2 * 0.15 + comLike2 * 0.05;
            post1.put("hotness", hotness1);
            post2.put("hotness", hotness2);
            return Double.compare(hotness2, hotness1);
        });
        return postMaps.subList(0, count <= postMaps.size() ? count : postMaps.size());
    }
}
