package com.thirteen.smp.controller;

import com.thirteen.smp.exception.UserNotExistsException;
import com.thirteen.smp.pojo.Msg;
import com.thirteen.smp.service.ChatService;
import com.thirteen.smp.utils.AccessTokenUtil;
import com.thirteen.smp.utils.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 聊天模块控制器
 *
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @RequestMapping(method = RequestMethod.POST)
    public Object sendChatMsg(@RequestBody Map<String, Object> param, HttpServletRequest request) {
        Integer currentUserId = AccessTokenUtil.getUserId(request);
        Integer targetUserId = (Integer) param.get("userId");
        String message = (String) param.get("message");
        Msg msg = new Msg();
        msg.setUserId(currentUserId);
        msg.setToUserId(targetUserId);
        msg.setContent(message);

        boolean res = false;
        try {
            res = chatService.sendMsg(msg);
        } catch (UserNotExistsException e) {
            return ResponseUtil.getErrorResponse(401);
        }
        if (res) {
            return ResponseUtil.getSuccessResponse();
        } else {
            return ResponseUtil.getErrorResponse(501);
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public Object getMsg(HttpServletRequest request, Integer userId) {
        Integer currUserId = AccessTokenUtil.getUserId(request);
        List<Msg> list = null;
        try {
            list = chatService.getMsg(currUserId, userId);
        } catch (UserNotExistsException e) {
            return ResponseUtil.getErrorResponse(401);
        }
        return ResponseUtil.getSuccessResponse(list);
    }

    @RequestMapping(value = "/getlist", method = RequestMethod.GET)
    public Object getChatList(HttpServletRequest request) {
        Integer userId = AccessTokenUtil.getUserId(request);
        List<Map<String, Object>> chatList = chatService.getChatList(userId);
        return ResponseUtil.getSuccessResponse(chatList);
    }

    @RequestMapping(value = "/read", method = RequestMethod.POST)
    public Object readMsg(@RequestBody Map<String, Object> param) {
        Integer msgId = (Integer) param.get("msgId");
        boolean res = chatService.setIsRead(msgId);
        if (res) {
            return ResponseUtil.getSuccessResponse();
        } else {
            return ResponseUtil.getErrorResponse(801);
        }
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public Object deleteChatHistory(HttpServletRequest request, Integer userId) {
        Integer currUserId = AccessTokenUtil.getUserId(request);
        int count = 0;
        try {
            count = chatService.deleteChat(currUserId, userId);
        } catch (UserNotExistsException e) {
            return ResponseUtil.getErrorResponse(401);
        }

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("deleteCount", count);
        return ResponseUtil.getSuccessResponse(map);
    }

    @RequestMapping(value = "/del", method = RequestMethod.DELETE)
    public Object deleteChatMsg(Integer msgId) {
        int res = chatService.deleteChatMsg(msgId);
        if (res == 0) {
            return ResponseUtil.getErrorResponse(801);
        }
        return ResponseUtil.getSuccessResponse();
    }

    @RequestMapping(value = "/count", method = RequestMethod.GET)
    public Object getNotReadCount(HttpServletRequest request) {
        Integer userId = AccessTokenUtil.getUserId(request);
        int notReadCount = chatService.getNotReadCount(userId);
        return ResponseUtil.getSuccessResponse(notReadCount);
    }

}
