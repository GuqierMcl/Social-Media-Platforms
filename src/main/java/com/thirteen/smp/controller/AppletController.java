package com.thirteen.smp.controller;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.thirteen.smp.response.ResponseData;
import com.thirteen.smp.service.global.GlobalVariables;
import com.thirteen.smp.utils.*;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 小程序控制器
 *
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
            return ResponseUtil.getErrorResponse(204);
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
                return ResponseUtil.getSuccessResponse(ipAddressToMap);
            }
        } else {
            // 如果给了ip参数，则为查询指定IP归属地
            Map<String, String> ipAddressToMap = IpAddressUtil.getIpAddressToMap(ip);
            if (ipAddressToMap == null) {
                return ResponseUtil.getResponseData(0, "内网IP或IP无效");
            } else {
                return ResponseUtil.getSuccessResponse(ipAddressToMap);
            }
        }
    }

    @RequestMapping(value = "/mail/send", method = RequestMethod.POST)
    public Object sendCodeEMail(@RequestBody Map<String, String> body) {
        String address = body.get("address");
        if (address == null) {
            return ResponseUtil.getErrorResponse(407); // 邮箱地址为空
        }

        // 创建配置对象
        Properties props = new Properties();
        // 表示SMTP发送邮件，需要进行身份验证
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.qq.com");
        //设置端口：
        //加密方式：
        props.put("mail.smtp.socketFactory.class", "jakarta.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.port", "587");
        props.put("mail.smtp.port", "587");

        props.put("mail.smtp.from", SettingUtil.getValue("mail.user"));    //mailfrom 参数
        props.put("mail.user", SettingUtil.getValue("mail.user"));// 发件人的账号（在控制台创建的发信地址）
        props.put("mail.password", SettingUtil.getValue("mail.password"));// 发信地址的smtp密码（在控制台选择发信地址进行设置）

        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // 用户名、密码
                String userName = props.getProperty("mail.user");
                String password = props.getProperty("mail.password");
                return new PasswordAuthentication(userName, password);
            }
        };

        //使用环境属性和授权信息，创建邮件会话
        Session mailSession = Session.getInstance(props, authenticator);
        MimeMessage message = new MimeMessage(mailSession);
        try {
            // 设置发件人邮件地址和名称。填写控制台配置的发信地址。和上面的mail.user保持一致。名称用户可以自定义填写。
            InternetAddress from = new InternetAddress(SettingUtil.getValue("mail.user"), "SMP");//from 参数,可实现代发，注意：代发容易被收信方拒信或进入垃圾箱。
            message.setFrom(from);

            // 设置收件人邮件地址
            InternetAddress to = new InternetAddress(address);
            message.setRecipient(MimeMessage.RecipientType.TO, to);
            message.setSentDate(new Date()); //设置时间

            // 调用工具类生成并获取验证码
            String code = null;
            for (; ; ) {
                code = VerificationCodeUtil.generateVerificationCode();// 调用工具类生成
                if (!GlobalVariables.GENERATED_CODE.contains(code)) { // 判断不重复，使每一次生成的验证码不是已生成的验证码
                    GlobalVariables.GENERATED_CODE.add(code); // 将生成的验证码添加到全局变量
                    System.out.println(code);
                    break;
                }
            }

            // 设置邮件内容
            message.setSubject("Our Social邮箱验证码 | SMP网络社交媒体平台");//设置邮件标题
            message.setContent(VerificationCodeUtil.getEmailContent(code), "text/html;charset=UTF-8");//html超文本；// "text/plain;charset=UTF-8" //纯文本。
            // 发送邮件，同时计算消耗实践
            long startTime = System.currentTimeMillis();   //获取开始时间（毫秒）
            Transport.send(message);
            long endTime = System.currentTimeMillis(); //获取结束时间（毫秒

            // 创建响应数据map
            Map<String, Object> data = new HashMap<>();
            data.put("targetAddress", address);
            data.put("runTime", (endTime - startTime) + "ms");

            // 响应数据
            return ResponseUtil.getSuccessResponse(data);
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return ResponseUtil.getResponseData(0, "发送邮件失败");
        }
    }

    @RequestMapping(value = "/mail/verify", method = RequestMethod.POST)
    public Object verifyEmailCode(@RequestBody Map<String, String> body){
        String code = body.get("code");
        if(code == null){
            return ResponseUtil.getErrorResponse(407);
        }

        if(GlobalVariables.GENERATED_CODE.contains(code)){
            GlobalVariables.GENERATED_CODE.remove(code);
            return ResponseUtil.getSuccessResponse("验证码校验通过");
        }
        return ResponseUtil.getErrorResponse(408);
    }
}