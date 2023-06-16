package com.thirteen.smp.controller;

import com.thirteen.smp.exception.PostNotExistException;
import com.thirteen.smp.exception.PubBannedWordsException;
import com.thirteen.smp.pojo.Post;
import com.thirteen.smp.response.ResponseData;
import com.thirteen.smp.service.PostService;
import com.thirteen.smp.utils.AccessTokenUtil;
import com.thirteen.smp.utils.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 帖子模块控制器
 *
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/post")
public class PostController {

    // TODO 张力文 编写POST模块接口控制器
    @Autowired
    private PostService postService;

    //根据id获取单个帖子
    @RequestMapping(path = "/getDetail", method = RequestMethod.GET)
    public ResponseData getPostById(HttpServletRequest request, @RequestParam("postId") String postId) {

        Map<String,Object> post=null;
        try {
             post = postService.getPostById(AccessTokenUtil.getUserId(request),Integer.parseInt(postId));
        } catch (PostNotExistException e) {
            return ResponseUtil.getErrorResponse(601);
        }
        return ResponseUtil.getSuccessResponse(post);
    }


    //获取帖子（获取自己的帖子/获取自己和关注列表用户的帖子）
    @RequestMapping(method = RequestMethod.GET)
    public ResponseData getPost(HttpServletRequest request, Integer userId) {
        List<Post> posts = null;
        List<Map<String,Object>> results=null;
        if (userId != null) {
            try {
                results = postService.getPostSelf(userId,AccessTokenUtil.getUserId(request));
                return ResponseUtil.getSuccessResponse(results);
            } catch (PostNotExistException e) {
                return ResponseUtil.getErrorResponse(604);
            }
        }else {
            try {
                results = postService.getPostSelfFollow(AccessTokenUtil.getUserId(request));
                return ResponseUtil.getSuccessResponse(results);
            } catch (PostNotExistException e) {
                return ResponseUtil.getErrorResponse(605);
            }
        }
    }

    //发布帖子
    @RequestMapping(method = RequestMethod.POST)
    public ResponseData savePost(HttpServletRequest request, @RequestParam("desc") String desc, @RequestParam("img") String img) {
        Post post = new Post(AccessTokenUtil.getUserId(request), desc, img);
        int count = 0;
        try {
            count = postService.savePost(post);
            if (count == -1) {
                return ResponseUtil.getErrorResponse(603);
            }
        } catch (PostNotExistException e) {
            return ResponseUtil.getErrorResponse(501);
        } catch (PubBannedWordsException e){
            return ResponseUtil.getErrorResponse(609);
        }
        return ResponseUtil.getSuccessResponse(null);//发布成功
    }

    //删除帖子
    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseData deletePost(HttpServletRequest request, @RequestParam("postId") int postId) {
        int count = 0;
        try {
            count = postService.deletePost(postId);
        } catch (PostNotExistException e) {
            return ResponseUtil.getErrorResponse(601);//帖子不存在
        }
        return ResponseUtil.getSuccessResponse(null);//删除成功
    }
}
