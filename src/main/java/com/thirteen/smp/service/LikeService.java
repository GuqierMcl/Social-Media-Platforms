package com.thirteen.smp.service;

import java.util.List;

/**
 * 点赞模块业务类
 * @author 张力文
 */

public interface LikeService {
    boolean giveLike(Integer postId, Integer userId);

    List<Integer> getLikeUserIdByPostId(Integer postId);

    boolean deleteLike(Integer postId, Integer userId);

    boolean giveCommentLike(Integer userId, Integer commentId);

    boolean deleteCommentLike(Integer userId, Integer commentId);
}
