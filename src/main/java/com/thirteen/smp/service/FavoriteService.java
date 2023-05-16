package com.thirteen.smp.service;

import com.thirteen.smp.pojo.Favorite;

import java.util.List;

public interface FavoriteService {
    List<Favorite> getFavorite(Integer userId);
    int addFavorite(Favorite favorite);
    int cancelFavorite(Integer userId,Integer postId);
}
