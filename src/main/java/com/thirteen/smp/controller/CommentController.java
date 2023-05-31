package com.thirteen.smp.controller;

import com.thirteen.smp.exception.CommentNotExistException;
import com.thirteen.smp.exception.PostNotExistException;
import com.thirteen.smp.pojo.Comment;
import com.thirteen.smp.response.ResponseData;
import com.thirteen.smp.service.CommentService;
import com.thirteen.smp.utils.AccessTokenUtil;
import com.thirteen.smp.utils.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Map;

/**
 * 评论模块控制器
 *
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/comment")
public class CommentController {

    // TODO 庄可欣 编写评论模块接口控制器
    @Autowired
    private CommentService commentService;

    @RequestMapping(value = "/get-number", method = RequestMethod.GET)
    public ResponseData getNumber(Integer postId) {
        try {
            return ResponseUtil.getSuccessResponse(commentService.getCount(postId));
        } catch (PostNotExistException e) {
            return ResponseUtil.getErrorResponse(601); //帖子不存在
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseData getComments(Integer postId, HttpServletRequest request) {
        Integer userId = AccessTokenUtil.getUserId(request);
        try {
            return ResponseUtil.getSuccessResponse(commentService.getComments(postId, userId));
        } catch (PostNotExistException e) {
            return ResponseUtil.getErrorResponse(601);
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseData publishComment(@RequestBody Map<String, Object> param, HttpServletRequest request) {
        Integer userId = AccessTokenUtil.getUserId(request);
        Comment comment = new Comment(null, (Integer) param.get("postId"), userId, (String) param.get("content"), new Timestamp(System.currentTimeMillis()), (Integer) param.get("commentId"));
        boolean res = false;

        try {
            res = commentService.publishComment(comment);
        } catch (PostNotExistException e) {
            return ResponseUtil.getErrorResponse(601);
        } catch (CommentNotExistException e) {
            return ResponseUtil.getErrorResponse(602); //评论不存在
        }
        if (res) {
            return ResponseUtil.getSuccessResponse(null);
        } else {
            return ResponseUtil.getErrorResponse(501); //更新数据库失败
        }
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseData deleteComment(Integer commentId) {
        boolean res = commentService.deleteComment(commentId);
        if (res) {
            return ResponseUtil.getSuccessResponse(null);
        } else {
            return ResponseUtil.getErrorResponse(501);
        }
    }
}
