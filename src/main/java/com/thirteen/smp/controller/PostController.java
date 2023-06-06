package com.thirteen.smp.controller;

import com.thirteen.smp.exception.PostNotExistException;
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

    //根据关键词搜索帖子
    @RequestMapping(path = "/searchPosts", method = RequestMethod.GET)
    public ResponseData queryPost(HttpServletRequest request, @RequestParam("type") String type, @RequestParam("query") String query) {
        if (type.equals("") || type == null || query.equals("") || query == null) {
            return ResponseUtil.getErrorResponse(606);
        }
        List<Post> posts = null;
        try {
            if (type.equals("home")) {
                posts = postService.queryPost(query);
            } else {
                posts = postService.queryPostSelf(query, AccessTokenUtil.getUserId(request));
            }
        } catch (PostNotExistException e) {
            return ResponseUtil.getErrorResponse(607);
        }
        return ResponseUtil.getSuccessResponse(posts);
    }

    //获取帖子（获取自己的帖子/获取自己和关注列表用户的帖子）
    @RequestMapping(method = RequestMethod.GET)
    public ResponseData getPost(HttpServletRequest request, Integer userId) {
        List<Post> posts = null;
        List<Map<String,Object>> results=null;
        if (userId != null) {
            try {
                results = postService.getPostSelf(userId,request);
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
