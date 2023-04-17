package com.thirteen.smp.interceptor;

import com.thirteen.smp.utils.ResponseUtil;
import com.thirteen.smp.utils.JwtUtil;
import com.thirteen.smp.utils.AccessTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * auth权限验证拦截器
 * @author 顾建平
 */
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取accessToken
        String accessToken = AccessTokenUtil.getAccessToken(request);

        // 如果没有accessToken信息，同样不予放行
        if (accessToken == null || accessToken.equals("")) {
            response.getWriter().print(ResponseUtil.parseAsJSON(ResponseUtil.getErrorRes(101)));
            return false;
        }

        // TODO 调试避开权限验证
        if (accessToken.equals("-1")) return true;

        try {
            JwtUtil.verifyToken(accessToken);//调用token解析的工具类进行解析
            return true;  //请求放行
        } catch (Exception e) {
            // 拦截请求并返回错误信息
            response.getWriter().print(ResponseUtil.parseAsJSON(ResponseUtil.getErrorRes(103)));
            return false;
        }
    }
}