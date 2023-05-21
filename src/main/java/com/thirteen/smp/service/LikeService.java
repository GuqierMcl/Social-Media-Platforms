package com.thirteen.smp.service;

import java.util.List;

/**
 * 点赞模块业务类
 * @author 张力文
 */

public interface LikeService {
    /**
     * 进行点赞
     * @param postId 帖子id userId 用户id
     * @return 是否点赞成功
     */
    boolean giveLike(Integer postId, Integer userId);
    /**
     * 获取点赞用户id
     * @param postId 帖子id
     * @return 返回点赞用户id列表
     */
    List<Integer> getLikeUserIdByPostId(Integer postId);
    /**
     * 取消点赞
     * @param postId 帖子id userId 用户id
     * @return 是否取消点赞成功
     */
    boolean deleteLike(Integer postId, Integer userId);
    /**
     * 进行评论点赞
     * @param commentId 评论id userId 用户id
     * @return 是否点赞成功
     */
    boolean giveCommentLike(Integer userId, Integer commentId);
    /**
     * 取消评论点赞
     * @param commentId 评论id userId 用户id
     * @return 是否取消点赞成功
     */
    boolean deleteCommentLike(Integer userId, Integer commentId);
}
