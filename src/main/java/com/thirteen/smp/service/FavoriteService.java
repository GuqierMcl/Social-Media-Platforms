package com.thirteen.smp.service;

import com.thirteen.smp.pojo.Favorite;

import java.util.List;
/**
 * 收藏夹模块业务接口
 * @author 庄可欣
 */
public interface FavoriteService {
    /**
     * 通过用户Id获取用户所有收藏夹信息
     * @param userId 当前用户Id
     * @return 收藏夹列表
     */
    List<Favorite> getFavorite(Integer userId);

    /**
     * 添加收藏
     * @param favorite 收藏夹对象
     * @return 收藏夹列表
     */
    int addFavorite(Favorite favorite);

    /**
     * 取消收藏
     * @param userId 当前用户Id
     * @param postId 帖子Id
     * @return 取消收藏夹条数
     */
    int cancelFavorite(Integer userId,Integer postId);
}
