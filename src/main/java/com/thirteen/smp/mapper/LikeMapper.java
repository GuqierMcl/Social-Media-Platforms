package com.thirteen.smp.mapper;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * 数据库t_like表Mapper接口
 *
 * @author 张力文
 * @version 1.0
 * @since 1.0
 */

public interface LikeMapper {
    /**
     * 通过帖子ID获取点赞数
     * @param postid 帖子ID
     * @return 点赞数量
     */
    List<Integer> getLikeUserIdByPostId(int postid);

    /**
     * 进行点赞
     * @param postid 帖子ID userid 用户id likeDate 点赞时间
     * @return 是否点赞成功
     */
    int giveLike(int postid, int userid, Timestamp likeDate);

    /**
     * 判断是否已经点过赞
     * @param postId 帖子ID  userid 用户id
     * @return 是否点过赞
     */
    int jugeLiked(int postId,int userid);
    /**
     * 取消点赞
     * @param postId 帖子ID userId用户id
     * @return 是否取消成功
     */
    int deleteLike(int postId,int userId);

    int insertCommentLike(Integer userId, Integer commentId, Timestamp time);

    int deleteCommentLike(Integer userId, Integer commentId);

    Integer selectCommentLikeNum(Integer commentId);

    Map<String,Object> selectCommentLikeById(Integer userId, Integer commentId);

}
