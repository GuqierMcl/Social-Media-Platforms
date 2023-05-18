package com.thirteen.smp.controller;

import com.thirteen.smp.exception.*;
import com.thirteen.smp.response.ResponseData;
import com.thirteen.smp.service.LikeService;
import com.thirteen.smp.utils.AccessTokenUtil;
import com.thirteen.smp.utils.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 点赞模块
 *
 * @author 张力文
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/like")
public class LikeController {
    @Autowired
    private LikeService likeService;

    //进行点赞
    @RequestMapping(method = RequestMethod.POST)
    public ResponseData giveLike(HttpServletRequest request, @RequestBody Map<String,Integer> postInfo){
        boolean res=false;
        try{
            res=likeService.giveLike(postInfo.get("postId"), AccessTokenUtil.getUserId(request));
        } catch (PostNotExistException e){
            return ResponseUtil.getErrorRes(601);
        } catch (UserNotExistsException e){
            return ResponseUtil.getErrorRes(401);
        } catch (LikeExistException e){
            return ResponseUtil.getErrorRes(701);
        }
        return ResponseUtil.getSuccessRes(null);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseData getLikeUserId(HttpServletRequest request, @RequestParam("postId") Integer postId){
        List<Integer> userIds=null;
        try{
            userIds = likeService.getLikeUserIdByPostId(postId);
        } catch (PostNotExistException e){
            return  ResponseUtil.getErrorRes(601);
        }
        return ResponseUtil.getSuccessRes(userIds);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseData deleteLike(HttpServletRequest request, @RequestParam("postId") Integer postId){
        boolean res=false;
        try{
            res=likeService.deleteLike(postId, AccessTokenUtil.getUserId(request));
            if(!res){
                return  ResponseUtil.getErrorRes(501);
            }
        } catch (PostNotExistException e){
            return ResponseUtil.getErrorRes(601);
        } catch (UserNotExistsException e){
            return ResponseUtil.getErrorRes(401);
        } catch (LikeExistException e){
            return ResponseUtil.getErrorRes(702);
        }
        return ResponseUtil.getSuccessRes(null);
    }

    @RequestMapping(value = "/comment",method = RequestMethod.POST)
    public Object addCommentLike(HttpServletRequest request, @RequestBody Map<String,Object> param){
        Integer userId = AccessTokenUtil.getUserId(request);
        Integer commentId = (Integer) param.get("commentId");
        boolean res;
        try {
            res = likeService.giveCommentLike(userId, commentId);
        } catch (LikeExistException e) {
            return ResponseUtil.getErrorRes(703);
        } catch (CommentNotExistException e){
            return ResponseUtil.getErrorRes(602);
        }
        if(!res){
            return ResponseUtil.getErrorRes(501);
        }
        return ResponseUtil.getSuccessRes();
    }

    @RequestMapping(value = "/comment",method = RequestMethod.DELETE)
    public Object deleteCommentLike(HttpServletRequest request, Integer commentId){
        Integer userId = AccessTokenUtil.getUserId(request);
        boolean res;
        try {
            res = likeService.deleteCommentLike(userId, commentId);
        } catch (LikeNotExistException e) {
            return ResponseUtil.getErrorRes(608);
        }catch (CommentNotExistException e){
            return ResponseUtil.getErrorRes(602);
        }
        if(!res){
            return ResponseUtil.getErrorRes(501);
        }
        return ResponseUtil.getSuccessRes();
    }

}
