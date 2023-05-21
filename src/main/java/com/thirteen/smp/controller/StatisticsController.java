package com.thirteen.smp.controller;

import com.thirteen.smp.response.ResponseData;
import com.thirteen.smp.service.StatisticsService;
import com.thirteen.smp.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    @Autowired
    StatisticsService statisticsService;

    @RequestMapping(path = "/user",method = RequestMethod.GET)
    public ResponseData getUserStatistics(){
        Map<String,Object> result = null;
        result=statisticsService.getUserStatistics();
        return ResponseUtil.getSuccessRes(result);
    }
    @RequestMapping(path = "/post",method = RequestMethod.GET)
    public ResponseData getPostNum(){
        Map<String,Object> result = null;
        result=statisticsService.getPostNum();
        return ResponseUtil.getSuccessRes(result);
    }
}