package com.thirteen.smp.controller;


import com.thirteen.smp.exception.HistoryNotExistException;
import com.thirteen.smp.exception.PostNotExistException;
import com.thirteen.smp.exception.UserNotExistsException;
import com.thirteen.smp.response.ResponseData;
import com.thirteen.smp.service.HistoryService;
import com.thirteen.smp.utils.AccessTokenUtil;
import com.thirteen.smp.utils.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * 浏览记录模块控制器
 *
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/history")
public class HistoryController {
    @Autowired
    HistoryService historyService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseData addHistory(HttpServletRequest request, @RequestBody Map<String,String> data){

        int userId = AccessTokenUtil.getUserId(request);
        try{
            historyService.addHistory(Integer.parseInt(data.get("postId")),userId);
            return ResponseUtil.getSuccessResponse(null);
        }
        catch (PostNotExistException e){
            return ResponseUtil.getErrorResponse(601);
        } catch (UserNotExistsException e){
            return ResponseUtil.getErrorResponse(401);
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseData SelectHistory(HttpServletRequest request){
        List<Map<String,Object>> historyList=null;
        int userId = AccessTokenUtil.getUserId(request);
        try{
            historyList = historyService.selectHistoryByUerId(userId);
            return ResponseUtil.getSuccessResponse(historyList);
        }
        catch (UserNotExistsException e){
            return ResponseUtil.getErrorResponse(401);
        }
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseData deleteAllHistory(HttpServletRequest request){
        Integer userId = AccessTokenUtil.getUserId(request);
        int count=0;
        try{
            count = historyService.deleteAllHistoryById(userId);
            if(count!=0) return ResponseUtil.getSuccessResponse(null);
            else return ResponseUtil.getErrorResponse(901);
        }
        catch (UserNotExistsException e){
            return ResponseUtil.getErrorResponse(401);
        }
    }

    @RequestMapping(path = "/single",method = RequestMethod.GET )
    public ResponseData deleteHistroyById(HttpServletRequest request,int id){
        Integer userId = AccessTokenUtil.getUserId(request);
        try{
            int i = historyService.deleteHistoryById(id, userId);
            if(i==-1){
                return ResponseUtil.getErrorResponse(902);
            }
            return ResponseUtil.getSuccessResponse(null);
        }
        catch (UserNotExistsException e){
            return ResponseUtil.getErrorResponse(401);
        }
        catch (HistoryNotExistException e){
            return ResponseUtil.getErrorResponse(903);
        }
    }
}
