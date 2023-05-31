package com.thirteen.smp.controller;

import com.thirteen.smp.exception.UserNotExistsException;
import com.thirteen.smp.response.ResponseData;
import com.thirteen.smp.service.FollowService;
import com.thirteen.smp.utils.AccessTokenUtil;
import com.thirteen.smp.utils.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 好友模块控制器
 *
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/relationship")
public class FollowController {

    @Autowired
    private FollowService followService;

    @RequestMapping(value = "/getFollowers", method = RequestMethod.GET)
    public ResponseData getFollowers(HttpServletRequest request) {
        Integer currentUserId = AccessTokenUtil.getUserId(request);
        List<Map<String, Object>> fansInfo = followService.getFansInfo(currentUserId);
        return ResponseUtil.getSuccessResponse(fansInfo);
    }

    @RequestMapping(value = "/getFollowedUsers", method = RequestMethod.GET)
    public ResponseData getFollowedUser(HttpServletRequest request) {
        Integer currentUserId = AccessTokenUtil.getUserId(request);
        List<Map<String, Object>> followedUser = followService.getFollowedUser(currentUserId);
        return ResponseUtil.getSuccessResponse(followedUser);
    }

    @RequestMapping(value = "/follow", method = RequestMethod.POST)
    public ResponseData follow(HttpServletRequest request, @RequestBody Map<String, Object> param) {
        Integer currentUserId = AccessTokenUtil.getUserId(request);
        Integer targetUserId = (Integer) param.get("userId");
        boolean res = false;
        try {
            res = followService.follow(currentUserId, targetUserId);
        } catch (UserNotExistsException e) {
            return ResponseUtil.getErrorResponse(401);
        }

        if (!res) {
            return ResponseUtil.getErrorResponse(501);
        } else {
            return ResponseUtil.getSuccessResponse(null);
        }
    }

    @RequestMapping(value = "/cancelFollow", method = RequestMethod.DELETE)
    public ResponseData canselFollow(HttpServletRequest request, Integer userId) {
        Integer currentUserId = AccessTokenUtil.getUserId(request);
        boolean res = false;
        try {
            res = followService.cancelFollow(currentUserId, userId);
        } catch (UserNotExistsException e) {
            return ResponseUtil.getErrorResponse(401);
        }

        if (!res) {
            return ResponseUtil.getErrorResponse(501);
        } else {
            return ResponseUtil.getSuccessResponse(null);
        }
    }


}
