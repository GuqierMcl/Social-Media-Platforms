package com.thirteen.smp.controller;

import com.thirteen.smp.service.HomeService;
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
 * 主页模块控制器
 *
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private HomeService homeService;

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public Object recommendUser(HttpServletRequest request, Integer count) {
        Integer userId = AccessTokenUtil.getUserId(request);
        int cnt;
        if (count == null) {
            cnt = 2;
        } else {
            cnt = count;
        }
        List<Map<String, Object>> recommendUser = homeService.getRecommendUser(cnt, userId);
        return ResponseUtil.getSuccessResponse(recommendUser);
    }

    @RequestMapping(value = "/post", method = RequestMethod.GET)
    public Object postUpdate(HttpServletRequest request, Integer count) {
        Integer userId = AccessTokenUtil.getUserId(request);
        int cnt;
        if (count == null) {
            cnt = 4;
        } else {
            cnt = count;
        }
        List<Map<String, Object>> postUpdate = homeService.getPostUpdate(cnt, userId);
        return ResponseUtil.getSuccessResponse(postUpdate);
    }

    @RequestMapping(value = "/online", method = RequestMethod.GET)
    public Object onlineFriend(HttpServletRequest request) {
        Integer userId = AccessTokenUtil.getUserId(request);
        List<Map<String, Object>> onlineFriend = homeService.getOnlineFriend(0, userId);
        return ResponseUtil.getSuccessResponse(onlineFriend);
    }

}
