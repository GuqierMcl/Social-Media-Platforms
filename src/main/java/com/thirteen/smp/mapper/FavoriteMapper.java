package com.thirteen.smp.mapper;

import com.thirteen.smp.pojo.Favorite;

import java.util.List;

public interface FavoriteMapper {
    List<Favorite> selectByUserId(Integer userId);
    Favorite selectById(Integer id);
    Favorite selectByUserIdAndPostId(Favorite favorite);
    int insertFavorite(Favorite favorite);
    int deleteById(Integer id);

    int delete(Integer userId,Integer postId);
}
