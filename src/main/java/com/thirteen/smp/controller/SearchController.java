package com.thirteen.smp.controller;

import com.thirteen.smp.exception.HistoryNotExistException;
import com.thirteen.smp.service.SearchService;
import com.thirteen.smp.utils.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    SearchService searchService;

    @RequestMapping(method = RequestMethod.GET)
    public Object globalSearch(String query, HttpServletRequest request){
        try{
            Map<String,Object> datas = searchService.globalSearch(query,request);
            return ResponseUtil.getSuccessRes(datas);
        } catch (HistoryNotExistException e){
            return ResponseUtil.getErrorRes(904);
        }
    }
}
