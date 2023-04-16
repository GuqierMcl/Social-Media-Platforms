package com.thirteen.smp.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.thirteen.smp.response.ResponseData;
import com.thirteen.smp.utils.ResponseUtil;
import com.thirteen.smp.utils.accessTokenUtil;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传控制器
 *
 * @author 顾建平
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/upload")
public class UploadController {

    private static final String imgSavePath = "/assets/store";

    @RequestMapping(method = RequestMethod.POST)
    public ResponseData fileUpload(MultipartFile img, HttpServletRequest request) {

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

        Integer userId = accessTokenUtil.getUserId(request);
        newName = userId + newName;

        try {
            // 转存文件到服务器
            img.transferTo(new File(folder, newName));

            // 创建响应对象
            ResponseData responseData = ResponseUtil.getResponseData(1);
//            ResponseData responseData = new ResponseData(1, "success", null);
//            responseData.addData("url", imgSavePath.replaceAll("/", "\\\\") + "\\" + newName);
            responseData.addData("url", imgSavePath.replaceAll("/", "\\\\") + "\\" + newName);

            // 模拟报错
            // String s = null;
            // s.toString();

            return responseData;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.getResponseData(202);
        }

    }
}