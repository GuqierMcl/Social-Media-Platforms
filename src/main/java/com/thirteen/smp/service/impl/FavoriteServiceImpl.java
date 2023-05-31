package com.thirteen.smp.service.impl;

import com.thirteen.smp.exception.PostNotExistException;
import com.thirteen.smp.mapper.*;
import com.thirteen.smp.pojo.Favorite;
import com.thirteen.smp.pojo.Post;
import com.thirteen.smp.pojo.User;
import com.thirteen.smp.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    private FavoriteMapper favoriteMapper;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private LikeMapper likeMapper;

    @Override
    public List<Map<String, Object>> getFavorite(Integer userId) {
        List<Favorite> favorites = favoriteMapper.selectByUserId(userId);
        List<Map<String, Object>> resultList = new ArrayList<>();

        for (Favorite favorite : favorites) {
            Map<String, Object> map = new HashMap<>();
            Post post = postMapper.selectByPostId(favorite.getPostId());
            User user = userMapper.selectById(post.getUserId());
            int commentCount = commentMapper.selectCountByPostId(post.getPostId());


            map.put("postId", post.getPostId());
            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            map.put("favoriteTime", dateformat.format(favorite.getTime()));
            map.put("PostTime", dateformat.format(post.getPostTime()));
            map.put("content", post.getContent());
            map.put("img", post.getImg());
            map.put("likeNum", post.getLikeNum());
            map.put("commentNum", commentMapper.selectCountByPostId(post.getPostId()));
            map.put("isLike", likeMapper.judgeLiked(post.getPostId(),userId));
            map.put("userId", user.getUserId());
            map.put("nickname", user.getNickname());
            map.put("profilePic", user.getProfilePic());

            resultList.add(map);
        }
        return resultList;
    }

    @Override
    @Transactional // 开启事务
    public int addFavorite(Favorite favorite) throws PostNotExistException {
        //判断帖子是否存在
        Post post = postMapper.selectByPostId(favorite.getPostId());
        if (post == null) {
            throw new PostNotExistException("帖子不存在");
        }
        //判断当前用户是否已收藏帖子
        Favorite favorite1 = favoriteMapper.selectByUserIdAndPostId(favorite);
        if (favorite1 != null) {
            return 0;
        }
        favorite.setTime(new Timestamp(new Date().getTime()));
        return favoriteMapper.insertFavorite(favorite);
    }

    @Override
    @Transactional
    public int cancelFavorite(Integer userId, Integer postId) {
        return favoriteMapper.delete(userId, postId);
    }
}
