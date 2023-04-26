package com.thirteen.smp.service.impl;

import com.thirteen.smp.exception.CommentNotExistException;
import com.thirteen.smp.exception.PostNotExistException;
import com.thirteen.smp.mapper.CommentMapper;
import com.thirteen.smp.mapper.PostMapper;
import com.thirteen.smp.mapper.UserMapper;
import com.thirteen.smp.pojo.Comment;
import com.thirteen.smp.pojo.Post;
import com.thirteen.smp.pojo.User;
import com.thirteen.smp.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public int getCount(Integer postId) throws PostNotExistException {
        Post post = postMapper.selectByPostId(postId);
        if (post == null) {
            throw new PostNotExistException("帖子不存在");
        }
        return commentMapper.selectCountByPostId(postId);
    }

    public List<Map<String, Object>> getComments(Integer postId) throws PostNotExistException {
        List<Map<String, Object>> resultList = new ArrayList<>();
        List<Comment> commentsLevel1 = new ArrayList<>();
        List<Comment> comments = commentMapper.selectByPostId(postId);

        Post post = postMapper.selectByPostId(postId);
        if (post == null) {
            throw new PostNotExistException("帖子不存在");
        }

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
            levelOne.put("content", comment.getCommentContent());

            User nowUser = userMapper.selectById(comment.getUserId());

            levelOne.put("name", nowUser.getNickname());    // 用户nickname
            levelOne.put("profilePic", nowUser.getProfilePic());  // 用户个人图片
            levelOne.put("createAt", comment.getCommentDate().toString());    // 发布时间

            // 查找子评论
            List<Map<String, Object>> subComments = new ArrayList<>();
            comments.forEach(c -> {
                if (c.getPreCommentId() != null) {
                    if (c.getPreCommentId().intValue() == comment.getCommentId().intValue()) {
                        Map<String, Object> levelTwo = new LinkedHashMap<>();

                        levelTwo.put("id", c.getCommentId());
                        levelTwo.put("content", c.getCommentContent());

                        User nowUserTwo = userMapper.selectById(c.getUserId());

                        levelTwo.put("name", nowUserTwo.getNickname());    // 用户nickname
                        levelTwo.put("profilePic", nowUserTwo.getProfilePic());  // 用户个人图片
                        levelTwo.put("createAt", c.getCommentDate().toString());    // 发布时间

                        subComments.add(levelTwo);
                    }
                }
            });

            levelOne.put("subComment", subComments);
            resultList.add(levelOne);
        });
        return resultList;
    }

    public boolean publishComment(Comment comment) throws PostNotExistException, CommentNotExistException {
        Post post = postMapper.selectByPostId(comment.getPostId());
        if (post == null) {
            throw new PostNotExistException("帖子不存在");
        }
        if (comment.getPreCommentId() != null) {
            if (commentMapper.selectByCommentId(comment.getPreCommentId()) == null) {
                throw new CommentNotExistException("评论不存在");
            }
        }
        int count = commentMapper.insertComment(comment);
        return count == 1;
    }

    public boolean deleteComment(Integer commentId) {
        int count = commentMapper.deleteCommentByCommentId(commentId);
        return count == 1;
    }
}
