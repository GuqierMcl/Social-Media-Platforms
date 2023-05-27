package com.thirteen.smp.service.impl;

import com.thirteen.smp.exception.CommentNotExistException;
import com.thirteen.smp.exception.PostNotExistException;
import com.thirteen.smp.mapper.CommentMapper;
import com.thirteen.smp.mapper.LikeMapper;
import com.thirteen.smp.mapper.PostMapper;
import com.thirteen.smp.mapper.UserMapper;
import com.thirteen.smp.pojo.Comment;
import com.thirteen.smp.pojo.Post;
import com.thirteen.smp.pojo.User;
import com.thirteen.smp.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CommentServiceImpl implements CommentService {

    // TODO 庄可欣 实现评论业务类
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LikeMapper likeMapper;

    @Override
    public int getCount(Integer postId) throws PostNotExistException {
        Post post = postMapper.selectByPostId(postId);
        if (post == null) {
            throw new PostNotExistException("帖子不存在");
        }
        return commentMapper.selectCountByPostId(postId); //返回帖子下的评论数
    }

    @Override
    public List<Map<String, Object>> getComments(Integer postId, Integer userId) throws PostNotExistException {
        List<Map<String, Object>> resultList = new ArrayList<>();
        List<Comment> commentsLevel1 = new ArrayList<>();
        List<Comment> comments = commentMapper.selectByPostId(postId); // 查询当前帖子的所有评论

        Post post = postMapper.selectByPostId(postId);
        if (post == null) {
            throw new PostNotExistException("帖子不存在");
        }

        /*
         * 查找第一层评论
         * 查找第二次评论
         * 拼接返回对象
         * */

        /*查找第一层评论*/
        comments.forEach(comment -> {
            if (comment.getPreCommentId() == null) {
                commentsLevel1.add(comment);
            }
        });

        /*将第一层评论封装到结果对象中，同时查找第一层评论的之评论*/
        commentsLevel1.forEach(comment -> {
            Map<String, Object> levelOne = new LinkedHashMap<>();
            levelOne.put("id", comment.getCommentId());

            User nowUser = userMapper.selectById(comment.getUserId());

            levelOne.put("userId", nowUser.getUserId());
            levelOne.put("content", comment.getCommentContent());

            levelOne.put("name", nowUser.getNickname());    // 用户nickname
            levelOne.put("profilePic", nowUser.getProfilePic());  // 用户个人图片
            levelOne.put("createAt", comment.getCommentDate().toString());    // 发布时间
            levelOne.put("likeNum", likeMapper.selectCommentLikeNum(comment.getCommentId()));    // 点赞数
            boolean isLike;
            Map<String, Object> map = likeMapper.selectCommentLikeById(userId, comment.getCommentId());
            isLike = map != null;
            levelOne.put("isLike", isLike);    // 当前用户是否点赞


            // 查找子评论
            List<Map<String, Object>> subComments = new ArrayList<>();
            comments.forEach(c -> {
                if (c.getPreCommentId() != null) {
                    if (c.getPreCommentId().intValue() == comment.getCommentId().intValue()) {
                        Map<String, Object> levelTwo = new LinkedHashMap<>();

                        levelTwo.put("id", c.getCommentId());
                        User nowUserTwo = userMapper.selectById(c.getUserId());
                        levelTwo.put("userId", nowUserTwo.getUserId());
                        levelTwo.put("content", c.getCommentContent());

                        levelTwo.put("name", nowUserTwo.getNickname());    // 用户nickname
                        levelTwo.put("profilePic", nowUserTwo.getProfilePic());  // 用户个人图片
                        levelTwo.put("createAt", c.getCommentDate().toString());    // 发布时间
                        levelTwo.put("likeNum", likeMapper.selectCommentLikeNum(c.getCommentId()));    // 发布时间
                        boolean flag;
                        Map<String, Object> map1 = likeMapper.selectCommentLikeById(userId, c.getCommentId());
                        flag = map1 != null;
                        levelTwo.put("isLike", flag);    // 发布时间


                        subComments.add(levelTwo);
                    }
                }
            });

            levelOne.put("subComment", subComments);
            resultList.add(levelOne);
        });
        return resultList;
    }

    @Override
    @Transactional
    public boolean publishComment(Comment comment) throws PostNotExistException, CommentNotExistException {
        Post post = postMapper.selectByPostId(comment.getPostId());
        if (post == null) {
            throw new PostNotExistException("帖子不存在");
        }
        if (comment.getPreCommentId() != null) {
            Comment preComment = commentMapper.selectByCommentId(comment.getPreCommentId());
            if (preComment == null) {
                throw new CommentNotExistException("评论不存在");
            }
            if (preComment.getPreCommentId() != null) {
                Integer userId = preComment.getUserId();
                User user = userMapper.selectById(userId);
                comment.setCommentContent("@" + user.getNickname() + ": " + comment.getCommentContent());
                comment.setPreCommentId(preComment.getPreCommentId());
            }
        }
        int count = commentMapper.insertComment(comment);
        return count == 1;
    }

    @Override
    @Transactional
    public boolean deleteComment(Integer commentId) {
        int count = commentMapper.deleteCommentByCommentId(commentId);
        return count == 1;
    }
}
