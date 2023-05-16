package com.thirteen.smp.service.impl;

import com.thirteen.smp.exception.PostNotExistException;
import com.thirteen.smp.mapper.FavoriteMapper;
import com.thirteen.smp.mapper.PostMapper;
import com.thirteen.smp.pojo.Favorite;
import com.thirteen.smp.pojo.Post;
import com.thirteen.smp.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    private FavoriteMapper favoriteMapper;

    @Autowired
    private PostMapper postMapper;

    @Override
    public List<Favorite> getFavorite(Integer userId) {
        return favoriteMapper.selectByUserId(userId);
    }

    @Override
    @Transactional // 开启事务
    public int addFavorite(Favorite favorite) throws PostNotExistException{

        Post post = postMapper.selectByPostId(favorite.getPostId());
        if(post==null){
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
    public int cancelFavorite(Integer userId,Integer postId) {
        return favoriteMapper.delete(userId,postId);
    }
}
