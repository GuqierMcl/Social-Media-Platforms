package com.thirteen.smp.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Logback日志记录切面类
 */
@Aspect
@Component
public class LogbackAspect {

    /**
     * 日志记录对象
     */
    public static final Logger LOGGER = LoggerFactory.getLogger(LogbackAspect.class);

    /**
     * 业务类前置通知
     * @param joinPoint 切入点对象
     */
    @Before("execution(public * com.thirteen.smp.service..*(..))")
    public void beforeServiceAdvice(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        LOGGER.info("Service 业务调用 " + joinPoint.getTarget().getClass().getName() + ":" + joinPoint.getSignature().getName() + " 参数 " + Arrays.toString(args));
    }

    /**
     * 业务类后置通知
     * @param joinPoint 切入点对象
     */
    @AfterReturning("execution(public * com.thirteen.smp.service..*(..))")
    public void afterReturningServiceAdvice(JoinPoint joinPoint) {
        LOGGER.info("Service 业务调用无异常结束 " + joinPoint.getSignature().getName());
    }

    /**
     * 业务类异常通知
     * @param joinPoint 切入点对象
     */
    @AfterThrowing(pointcut = "execution(public * com.thirteen.smp.service..*(..))", throwing = "e")
    public void afterThrowingServiceAdvice(JoinPoint joinPoint, Exception e) {
        LOGGER.info("Service 业务调用异常 " + joinPoint.getSignature().getName() + "Exception:" + e.getMessage());
    }

    /**
     * 控制器前置通知
     * @param joinPoint 切入点对象
     */
    @Before("execution(public * com.thirteen.smp.controller..*(..))")
    public void beforeControllerAdvice(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        LOGGER.info("Controller 接口调用 " + joinPoint.getTarget().getClass().getName() + ":" + joinPoint.getSignature().getName() + " 参数 " + Arrays.toString(args));
    }

    /**
     * Mapper接口前置通知
     * @param joinPoint 切入点对象
     */
    @Before("execution(public * com.thirteen.smp.mapper..*(..))")
    public void beforeMapperAdvice(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        LOGGER.info("Mapper 映射调用 " + joinPoint.getTarget().getClass().getName() + ":" + joinPoint.getSignature().getName() + " 参数 " + Arrays.toString(args));
    }
}
