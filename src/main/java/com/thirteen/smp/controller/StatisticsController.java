package com.thirteen.smp.controller;

import com.thirteen.smp.response.ResponseData;
import com.thirteen.smp.service.StatisticsService;
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
 * 统计模块控制器
 *
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    @Autowired
    StatisticsService statisticsService;

    @RequestMapping(path = "/user", method = RequestMethod.GET)
    public ResponseData getUserStatistics() {
        List<Map<String, Object>> result = null;
        result = statisticsService.getUserStatistics();
        return ResponseUtil.getSuccessResponse(result);
    }

    @RequestMapping(path = "/post", method = RequestMethod.GET)
    public ResponseData getPostNum() {
        List<Integer> result = null;
        result = statisticsService.getPostNum();
        return ResponseUtil.getSuccessResponse(result);
    }

    @RequestMapping(value = "/hotUser", method = RequestMethod.GET)
    public Object getHotUserList(Integer count, HttpServletRequest request) {
        Integer userId = AccessTokenUtil.getUserId(request);
        int cnt;
        if (count == null) {
            cnt = 10;
        } else {
            cnt = count;
        }
        List<Map<String, Object>> hotUserList = null;
        try {
//            hotUserList = statisticsService.getHotUserList(cnt, userId);
            hotUserList = statisticsService.getGeneratedHotUserList(cnt, userId);// 使用已生成的排行榜列表
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.getErrorResponse(0);
        }
        return ResponseUtil.getSuccessResponse(hotUserList);
    }

    @RequestMapping(value = "/hotPost", method = RequestMethod.GET)
    public Object getHotPostList(Integer count) {
        int cnt;
        if (count == null) {
            cnt = 10;
        } else {
            cnt = count;
        }
        List<Map<String, Object>> hotPostList = null;
        try {
//            hotPostList = statisticsService.getHotPostList(cnt);
            hotPostList = statisticsService.getGeneratedHotPostList(cnt);// 使用已生成的排行榜列表
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.getErrorResponse(0);
        }
        return ResponseUtil.getSuccessResponse(hotPostList);
    }
}
