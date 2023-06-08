package com.thirteen.smp.exception.resolver;

import com.thirteen.smp.utils.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

/**
 * 全局异常处理类
 * @version 1.0
 * @since 1.0
 */
@Component
public class GlobalServiceExceptionResolver implements HandlerExceptionResolver {

    /**
     * 全局异常处理方法
     * @param request 请求HttpRequest
     * @param response 请求HttpResponse
     * @param handler 请求目标对象
     * @param ex 全局异常
     * @return MV模型
     */
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 获取异常消息
        String message = ex.getMessage();
        try {
            // 响应异常消息
            response.getWriter().print(ResponseUtil.parseAsJSON(ResponseUtil.getResponseData(0, message)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new ModelAndView();
    }
}
