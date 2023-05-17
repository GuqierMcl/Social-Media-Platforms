package com.thirteen.smp.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.thirteen.smp.response.ResponseData;
import com.thirteen.smp.utils.IpAddressUtil;
import com.thirteen.smp.utils.ResponseUtil;
import com.thirteen.smp.utils.AccessTokenUtil;
import com.thirteen.smp.utils.SettingUtil;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 小程序控制器
 *
 * @author 顾建平
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/applet")
public class AppletController {

    private static final String imgSavePath = SettingUtil.getValue("imgSavingPath");

    @RequestMapping(path = "/upload", method = RequestMethod.POST)
    public ResponseData fileUpload(MultipartFile img, HttpServletRequest request) {
        if (img == null) {
            return ResponseUtil.getErrorRes(204);
        }

        //设置时间戳格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String format = sdf.format(new Date());

        // 获取保存目录的绝对路径
        String realPath = request.getServletContext().getRealPath(imgSavePath);

        // 将保存目录的绝对路径转化为File对象
        File folder = new File(realPath);

        // 如果保存目录不存在则创建目录
        if (!folder.exists()) {
            if (!folder.mkdirs()) {
                return ResponseUtil.getResponseData(203);
            }
        }
        // 获取上传文件文件名
        String oldName = img.getOriginalFilename();
        if (oldName == null) {
            return ResponseUtil.getResponseData(201);
        }

        // 拼接保存文件文件名（时间戳 + 后缀）
        String newName =/*userId +*/ format + oldName.substring(oldName.lastIndexOf("."));

        Integer userId = AccessTokenUtil.getUserId(request);
        newName = userId + newName;

        try {
            // 转存文件到服务器
            img.transferTo(new File(folder, newName));

            // 创建响应对象
            ResponseData responseData = ResponseUtil.getResponseData(1);
//            ResponseData responseData = new ResponseData(1, "success", null);
//            responseData.addData("url", imgSavePath.replaceAll("/", "\\\\") + "\\" + newName);
            responseData.addData("url", SettingUtil.getValue("serverPath") + imgSavePath + "/" + newName);

            // 模拟报错
            // String s = null;
            // s.toString();

            return responseData;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.getResponseData(202);
        }
    }

    @RequestMapping(value = "/ip", method = RequestMethod.GET)
    public Object ipAddress(HttpServletRequest request, String ip) {
        System.out.println(ip);
        if (ip == null || "".equals(ip)) {
            // 没有ip参数则为查询请求ip归属地
            // 获取请求ip
            String realIp = request.getHeader("x-forwarded-for");
            if (realIp == null || realIp.length() == 0 || "unknown".equalsIgnoreCase(realIp)) {
                realIp = request.getHeader("Proxy-Client-IP");
            }
            if (realIp == null || realIp.length() == 0 || "unknown".equalsIgnoreCase(realIp)) {
                realIp = request.getHeader("HTTP_CLIENT_IP");
            }
            if (realIp == null || realIp.length() == 0 || "unknown".equalsIgnoreCase(realIp)) {
                realIp = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (realIp == null || realIp.length() == 0 || "unknown".equalsIgnoreCase(realIp)) {
                realIp = request.getRemoteAddr();
            }
            Map<String, String> ipAddressToMap = IpAddressUtil.getIpAddressToMap(realIp);
            if (ipAddressToMap == null) {
                return ResponseUtil.getResponseData(0, "内网IP或IP无效");
            } else {
                return ResponseUtil.getSuccessRes(ipAddressToMap);
            }
        } else {
            // 如果给了ip参数，则为查询指定IP归属地
            Map<String, String> ipAddressToMap = IpAddressUtil.getIpAddressToMap(ip);
            if (ipAddressToMap == null) {
                return ResponseUtil.getResponseData(0, "内网IP或IP无效");
            } else {
                return ResponseUtil.getSuccessRes(ipAddressToMap);
            }
        }
    }
}