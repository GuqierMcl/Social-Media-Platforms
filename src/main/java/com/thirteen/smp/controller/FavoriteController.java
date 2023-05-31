package com.thirteen.smp.controller;

import com.thirteen.smp.exception.PostNotExistException;
import com.thirteen.smp.pojo.Favorite;
import com.thirteen.smp.service.FavoriteService;
import com.thirteen.smp.utils.AccessTokenUtil;
import com.thirteen.smp.utils.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 收藏夹模块控制器
 *
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/favorite")
public class FavoriteController {
    @Autowired
    private FavoriteService favoriteService;

    @RequestMapping(method = RequestMethod.GET)
    public Object getFavorite(HttpServletRequest request) {
        Integer userId = AccessTokenUtil.getUserId(request);
        List<Map<String, Object>> favorite = favoriteService.getFavorite(userId);
        return ResponseUtil.getSuccessResponse(favorite);
    }

    @RequestMapping(method = RequestMethod.POST)
    public Object addFavorite(HttpServletRequest request, Integer postId) {
        Integer userId = AccessTokenUtil.getUserId(request);
        int result = 0;
        try {
            result = favoriteService.addFavorite(new Favorite(null, postId, userId, null));
        } catch (PostNotExistException e) {
            return ResponseUtil.getErrorResponse(601);//帖子不存在
        }
        if (result == 1) {
            return ResponseUtil.getSuccessResponse();
        } else {
            return ResponseUtil.getErrorResponse(501);//数据库更新失败
        }
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public Object cancelFavorite(HttpServletRequest request, Integer postId) {
        Integer userId = AccessTokenUtil.getUserId(request);
        int result = favoriteService.cancelFavorite(userId, postId);
        if (result == 0) {
            return ResponseUtil.getErrorResponse(501);//数据库更新失败
        } else {
            return ResponseUtil.getSuccessResponse();
        }
    }
}
