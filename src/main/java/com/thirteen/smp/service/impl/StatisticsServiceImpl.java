package com.thirteen.smp.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thirteen.smp.mapper.*;
import com.thirteen.smp.pojo.Comment;
import com.thirteen.smp.pojo.Favorite;
import com.thirteen.smp.pojo.Post;
import com.thirteen.smp.pojo.User;
import com.thirteen.smp.service.StatisticsService;
import com.thirteen.smp.service.global.GlobalVariables;
import com.thirteen.smp.utils.ProvinceMapperUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

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

    /**
     * 排行榜刷新时间
     */
    private static final Integer REFRESH_TIME = 1000 * 60 * 5;

    @Override
    public List<Map<String, Object>> getUserStatistics() {
        List<User> users = userMapper.selectAll();
        List<Map<String, Object>> res = new ArrayList<>();
        List<Map<String, Object>> provinceMapList = ProvinceMapperUtil.getProvinceMapList();
        for (Map<String, Object> map : provinceMapList) {
            Map<String, Object> one = new HashMap<>();
            String name = (String) map.get("name");
            int cnt = 0;
            for (User user : users) {
                if (user.getUserLocation().equals(name)) {
                    cnt++;
                }
            }
            one.put("name", name);
            List<Object> value = new ArrayList<>();
            value.add(map.get("lon"));
            value.add(map.get("lat"));
            value.add(cnt);
            one.put("value", value);
            res.add(one);
        }
        return res;
    }

    @Override
    public List<Integer> getPostNum() {
        Map<String, Object> result = new LinkedHashMap<>();
        List<Map<String, Integer>> datas = new ArrayList<>();
        List<Post> posts = postMapper.selectAllPost();
        List<Post> postList = new ArrayList<>();
        Timestamp endTimestamp = new Timestamp(System.currentTimeMillis());
        List<Long> timeLimit = new ArrayList<>();
        for (int i = 1; i <= 7; i++) {
            Map<String, Integer> data = new LinkedHashMap<>();
            data.put("id", i);
            data.put("num", 0);
            datas.add(data);
            timeLimit.add((long) i * 1000 * 60 * 60 * 24);
        }
        for (Map<String, Integer> data : datas) {
            int num = 0;
            for (Post post : posts) {
                if (data.get("id") == 1) {
                    if (endTimestamp.getTime() - post.getPostTime().getTime() < timeLimit.get(data.get("id") - 1)) {
                        num++;
                    }
                } else {
                    if ((endTimestamp.getTime() - post.getPostTime().getTime()) < timeLimit.get(data.get("id") - 1) && (endTimestamp.getTime() - post.getPostTime().getTime()) >= timeLimit.get(data.get("id") - 2)) {
                        num++;
                    }
                }
            }
            data.replace("num", num);
        }
        List<Integer> res = new ArrayList<>();
        for (Map<String, Integer> data : datas) {
            res.add(data.get("num"));
        }
        Collections.reverse(res);
        return res;
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


    @Override
    public List<Map<String, Object>> getGeneratedHotUserList(Integer count, Integer userId) throws Exception {
        if(GlobalVariables.HOT_USER_LIST == null){
            refreshHotUserListTask();
            System.out.println("Get refresh user list");
        }else {
            System.out.println("Get generated user list");
        }
        List<Map<String, Object>> hotUserList = (List<Map<String, Object>>) GlobalVariables.HOT_USER_LIST.get("list");
        for (Map<String, Object> map : hotUserList) {
            map.put("isFollowing", followMapper.selectByUserId(userId, (Integer) map.get("userId")) != null);
        }
        return hotUserList;
    }

    @Override
    public List<Map<String, Object>> getGeneratedHotPostList(Integer count) throws Exception {
        if(GlobalVariables.HOT_USER_LIST == null){
            refreshHotPostListTask();
            System.out.println("Get refresh post list");
        }else {
            System.out.println("Get generated post list");
        }
        return (List<Map<String, Object>>)GlobalVariables.HOT_POST_LIST.get("list");
    }

    /**
     * 刷新用户热度排行榜定时任务
     * @throws Exception 异常
     */
    @Async
    @Scheduled(cron = "0 */5 * * * *")// 每五分钟刷新一次
    public void refreshHotUserListTask() throws Exception {
        Logger logger = LoggerFactory.getLogger(StatisticsServiceImpl.class);
        if (GlobalVariables.HOT_USER_LIST == null) {
            List<Map<String, Object>> hotUserList = getHotUserList(10, 8);
            GlobalVariables.HOT_USER_LIST = new HashMap<>();
            GlobalVariables.HOT_USER_LIST.put("time", new Date());
            GlobalVariables.HOT_USER_LIST.put("list", hotUserList);
            logger.info("New Hot User List");
        } else {
            Date time = (Date) GlobalVariables.HOT_USER_LIST.get("time");
            // 判断时间是否大于五分钟
            if (new Date().getTime() - time.getTime() >= REFRESH_TIME) {
                List<Map<String, Object>> hotUserList = getHotUserList(10, 8);
                GlobalVariables.HOT_USER_LIST = new HashMap<>();
                GlobalVariables.HOT_USER_LIST.put("time", new Date());
                GlobalVariables.HOT_USER_LIST.put("list", hotUserList);
                logger.info("Refresh Hot User List");
            }
        }
    }

    /**
     * 刷新帖子热度排行榜定时任务
     * @throws Exception 异常
     */
    @Async
    @Scheduled(cron = "0 */5 * * * *") // 每五分钟刷新一次
    public void refreshHotPostListTask() throws Exception {
        Logger logger = LoggerFactory.getLogger(StatisticsServiceImpl.class);
        if (GlobalVariables.HOT_POST_LIST == null) {
            List<Map<String, Object>> hotPostList = getHotPostList(10);
            GlobalVariables.HOT_POST_LIST = new HashMap<>();
            GlobalVariables.HOT_POST_LIST.put("time", new Date());
            GlobalVariables.HOT_POST_LIST.put("list", hotPostList);
            logger.info("New Hot Post List");
        } else {
            Date time = (Date) GlobalVariables.HOT_POST_LIST.get("time");
            // 判断时间是否大于五分钟
            if (new Date().getTime() - time.getTime() >= REFRESH_TIME) {
                List<Map<String, Object>> hotPostList = getHotPostList(10);
                GlobalVariables.HOT_POST_LIST = new HashMap<>();
                GlobalVariables.HOT_POST_LIST.put("time", new Date());
                GlobalVariables.HOT_POST_LIST.put("list", hotPostList);
                logger.info("Refresh Hot Post List");
            }
        }
    }
}
