package com.thirteen.smp.mapper;

import com.thirteen.smp.pojo.Comment;
import com.thirteen.smp.pojo.Post;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * 数据库t_like表Mapper接口
 *
 * @version 1.0
 * @since 1.0
 */

public interface LikeMapper {
    /**
     * 通过帖子ID获取点赞数
     * @param postId 帖子ID
     * @return 点赞数量
     */
    List<Integer> getLikeUserIdByPostId(int postId);

    /**
     * 进行点赞
     * @param postId 帖子ID userid 用户id likeDate 点赞时间
     * @return 是否点赞成功
     */
    int giveLike(int postId, int userId, Timestamp likeDate);

    /**
     * 判断是否已经点过赞
     * @param postId 帖子ID  userId 用户id
     * @return 是否点过赞
     */
    int judgeLiked(int postId,int userId);
    /**
     * 取消点赞
     * @param postId 帖子ID userId用户id
     * @return 是否取消成功
     */
    int deleteLike(int postId,int userId);

    /**
     * 查询指定用户点赞的帖子列表
     * @param userId 用户ID
     * @return 帖子列表
     */
    List<Post> selectLikePostByUserId(Integer userId);

    /**
     * 查询指定用户点赞的评论列表
     * @param userId 用户ID
     * @return 评论列表
     */
    List<Comment> selectLikeCommentByUserId(Integer userId);

    /**
     * 新增评论点赞
     * @param userId 用户ID
     * @param commentId 评论ID
     * @param time 点赞时间
     * @return 执行结果
     */
    int insertCommentLike(Integer userId, Integer commentId, Timestamp time);

    /**
     * 删除评论点赞
     * @param userId 用户ID
     * @param commentId 评论ID
     * @return 执行结果
     */
    int deleteCommentLike(Integer userId, Integer commentId);

    /**
     * 根据评论ID查询点赞数
     * @param commentId 评论ID
     * @return 点赞数
     */
    Integer selectCommentLikeNum(Integer commentId);

    /**
     * 根据用户ID和评论ID查询评论
     * @param userId 用户ID
     * @param commentId 评论ID
     * @return 查询结果
     */
    Map<String,Object> selectCommentLikeById(Integer userId, Integer commentId);

}
