package com.thirteen.smp.service.impl;

import com.thirteen.smp.exception.*;
import com.thirteen.smp.mapper.*;
import com.thirteen.smp.pojo.*;
import com.thirteen.smp.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
public class LikeServiceImpl implements LikeService {

    @Autowired
    private LikeMapper likemapper;
    @Autowired
    private UserMapper usermapper;
    @Autowired
    private PostMapper postmapper;
    @Autowired
    private CommentMapper commentMapper;

    @Override
    @Transactional//开启事务
    public boolean giveLike(Integer postId, Integer userId) {
        Post post = postmapper.selectByPostId(postId);
        User user = usermapper.selectById(userId);
        if (post == null) {
            throw new PostNotExistException("帖子不存在");
        }

        if (user == null) {
            throw new UserNotExistsException("用户不存在");
        }
        int flag = 0;
        flag = likemapper.judgeLiked(postId, userId);
        if (flag != 0) {
            throw new LikeExistException("该用户已经点赞过了");
        }
        int res = likemapper.giveLike(postId, userId, new Timestamp(new Date().getTime()));
        Integer res2 = postmapper.likePost(postId);
        return res == 1 && res2 == 1;
    }

    @Override
    public List<Integer> getLikeUserIdByPostId(Integer postId) {
        Post post = postmapper.selectByPostId(postId);
        if (post == null) {
            throw new PostNotExistException("帖子不存在");
        }
        List<Integer> userIds = null;
        userIds = likemapper.getLikeUserIdByPostId(postId);
        return userIds;
    }

    @Override
    public boolean deleteLike(Integer postId, Integer userId) {
        Post post = postmapper.selectByPostId(postId);
        User user = usermapper.selectById(userId);
        if (post == null) {
            throw new PostNotExistException("帖子不存在");
        }

        if (user == null) {
            throw new UserNotExistsException("用户不存在");
        }
        int flag = 0;
        flag = likemapper.judgeLiked(postId, userId);
        if (flag == 0) {
            throw new LikeExistException("该用户还没点赞");
        }
        int res = likemapper.deleteLike(postId, userId);
        int res2 = postmapper.unlikePost(postId);
        return res == 1 && res2 == 1;
    }

    @Override
    public boolean giveCommentLike(Integer userId, Integer commentId) {
        if (commentId == null) return false;
        Comment comment = commentMapper.selectByCommentId(commentId);
        if (comment == null) throw new CommentNotExistException();
        Map<String, Object> map = likemapper.selectCommentLikeById(userId, commentId);
        if (map != null) throw new LikeExistException();

        int res = likemapper.insertCommentLike(userId, commentId, new Timestamp(new Date().getTime()));
        return res == 1;
    }

    @Override
    public boolean deleteCommentLike(Integer userId, Integer commentId) {
        if (commentId == null) return false;
        Comment comment = commentMapper.selectByCommentId(commentId);
        if (comment == null) throw new CommentNotExistException();
        Map<String, Object> map = likemapper.selectCommentLikeById(userId, commentId);
        if (map == null) throw new LikeNotExistException();

        int res = likemapper.deleteCommentLike(userId, commentId);
        return res == 1;
    }
}
