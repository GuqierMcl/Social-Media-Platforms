package com.thirteen.smp.controller;

import com.thirteen.smp.service.HomeService;
import com.thirteen.smp.utils.AccessTokenUtil;
import com.thirteen.smp.utils.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private HomeService homeService;

    @RequestMapping("/user")
    public Object recommendUser(HttpServletRequest request, Integer count) {
        Integer userId = AccessTokenUtil.getUserId(request);
        Integer cnt = null;
        if (count == null) {
            cnt = 2;
        } else {
            cnt = count;
        }
        List<Map<String, Object>> recommendUser = homeService.getRecommendUser(cnt, userId);
        return ResponseUtil.getSuccessRes(recommendUser);
    }

}
