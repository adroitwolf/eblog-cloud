package com.blog.aop;

import com.blog.entity.model.ClickStatus;
import com.blog.service.RedisService;
import com.common.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * <pre>BlogClickAspect</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-06-01
 */
@Component
@Aspect
@Slf4j
public class BlogClickAspect {

    @Autowired
    RedisService redisService;

    @Around("@annotation(com.blog.aop.annotation.IncrementClickCount)")
    public Object incrementBlogClick(ProceedingJoinPoint joinPoint) throws Throwable {

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String remoteIp = CommonUtils.getRemoteIp(request);
        String sessionId = request.getRequestedSessionId();

        Object[] args = joinPoint.getArgs();

        Long blogId = (Long) args[0];

        ClickStatus clickStatus = new ClickStatus();
        clickStatus.setBlogId(blogId);
        clickStatus.setCount(1);
        clickStatus.setRemoteIp(remoteIp);
        clickStatus.setSessionId(sessionId);
        redisService.incrementBlogClickedCount(clickStatus);
        return joinPoint.proceed();
    }
}
