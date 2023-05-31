package com.thirteen.smp.mapper;

import com.thirteen.smp.pojo.Favorite;

import java.util.List;

/**
 * 用于t_favorite表数据库操作的Mapper
 * @author 庄可欣
 * @version 1.0
 * @since 1.0
 */
public interface FavoriteMapper {
    /**
     * 通过用户Id获取当前用户收藏夹列表
     * @param userId 用户Id
     * @return 收藏夹列表
     */
    List<Favorite> selectByUserId(Integer userId);

    /**
     * 通过收藏夹id获取收藏夹信息
     * @param id 收藏夹id
     * @return 收藏夹对象
     */
    Favorite selectById(Integer id);

    /**
     * 通过用户Id和帖子Id获取收藏夹信息
     * @param favorite
     * @return 收藏夹对象
     */
    Favorite selectByUserIdAndPostId(Favorite favorite);

    /**
     * 通过帖子ID获取收藏信息
     * @param postId 帖子ID
     * @return 收藏列表
     */
    List<Favorite> selectByPostId(Integer postId);

    /**
     * 添加收藏
     * @param favorite 收藏夹信息
     * @return 添加收藏夹信息条数
     */
    int insertFavorite(Favorite favorite);

    /**
     * 通过id取消收藏
     * @param id 收藏夹id
     * @return 取消收藏夹信息条数
     */
    int deleteById(Integer id);

    /**
     * 通过用户Id和帖子Id取消收藏
     * @param userId 当前用户Id
     * @param postId 帖子Id
     * @return 取消收藏夹信息条数
     */
    int delete(Integer userId,Integer postId);
}
