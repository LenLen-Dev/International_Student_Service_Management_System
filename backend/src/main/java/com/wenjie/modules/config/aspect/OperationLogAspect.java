package com.wenjie.modules.config.aspect;

import cn.dev33.satoken.stp.StpUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wenjie.modules.config.annotation.OperationLog;
import com.wenjie.modules.config.entity.SysOperationLog;
import com.wenjie.modules.config.mapper.SysOperationLogMapper;
import com.wenjie.modules.system.entity.SysUser;
import com.wenjie.modules.system.mapper.SysUserMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.List;

@Aspect
@Component
@RequiredArgsConstructor
public class OperationLogAspect {

    private final SysOperationLogMapper operationLogMapper;
    private final SysUserMapper userMapper;
    private final ObjectMapper objectMapper;

    @Around("@annotation(operationLog)")
    public Object around(ProceedingJoinPoint point, OperationLog operationLog) throws Throwable {
        long begin = System.currentTimeMillis();
        Object result = null;
        Throwable throwable = null;
        try {
            result = point.proceed();
            return result;
        } catch (Throwable ex) {
            throwable = ex;
            throw ex;
        } finally {
            saveLog(point, operationLog, result, throwable, System.currentTimeMillis() - begin);
        }
    }

    private void saveLog(ProceedingJoinPoint point, OperationLog operationLog, Object result, Throwable throwable, long costTime) {
        try {
            SysOperationLog log = new SysOperationLog();
            log.setModuleName(operationLog.module());
            log.setOperationType(operationLog.operationType());
            log.setOperationName(operationLog.operationName());
            log.setControllerMethod(controllerMethod(point));
            log.setRequestParams(limit(toJson(filterArgs(point.getArgs())), 3500));
            log.setResponseResult(limit(toJson(result), 3500));
            log.setSuccess(throwable == null ? 1 : 0);
            log.setErrorMessage(throwable == null ? null : limit(throwable.getMessage(), 3500));
            log.setCostTime(costTime);
            fillRequest(log);
            fillOperator(log);
            operationLogMapper.insert(log);
        } catch (Exception ignored) {
            // 日志记录不能影响主业务流程。
        }
    }

    private void fillRequest(SysOperationLog log) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return;
        }
        HttpServletRequest request = attributes.getRequest();
        log.setRequestMethod(request.getMethod());
        log.setRequestUri(request.getRequestURI());
        log.setIpAddress(clientIp(request));
        log.setUserAgent(limit(request.getHeader("User-Agent"), 500));
    }

    private void fillOperator(SysOperationLog log) {
        try {
            Long userId = Long.valueOf(String.valueOf(StpUtil.getLoginId()));
            SysUser user = userMapper.selectById(userId);
            log.setOperatorId(userId);
            log.setOperatorName(user == null ? String.valueOf(userId) : user.getRealName());
        } catch (Exception ignored) {
            log.setOperatorName("匿名用户");
        }
    }

    private String controllerMethod(ProceedingJoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        return signature.getDeclaringTypeName() + "." + signature.getMethod().getName();
    }

    private List<Object> filterArgs(Object[] args) {
        return Arrays.stream(args)
                .filter(arg -> !(arg instanceof HttpServletRequest))
                .filter(arg -> !(arg instanceof HttpServletResponse))
                .toList();
    }

    private String clientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        String realIp = request.getHeader("X-Real-IP");
        if (realIp != null && !realIp.isBlank()) {
            return realIp;
        }
        return request.getRemoteAddr();
    }

    private String toJson(Object value) {
        if (value == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(value);
        } catch (Exception ex) {
            return String.valueOf(value);
        }
    }

    private String limit(String text, int max) {
        if (text == null || text.length() <= max) {
            return text;
        }
        return text.substring(0, max);
    }
}
