package com.agritechiot.IoT.config;

import com.agritechiot.IoT.constant.GenConstant;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ApiAop {

    @Around("execution(* com.agritechiot.IoT.controller.*.*(..))")
    public Object flow(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            Object[] args = joinPoint.getArgs();
            if (args.length > 0 && args[0] != null) { // Check if args is not empty and first argument is not null
                MDC.put(GenConstant.CORRELATION_ID, args[0].toString()); // Convert the argument to a string
            } else {
                MDC.put(GenConstant.CORRELATION_ID, GenConstant.DEFAULT_CORRELATION_ID);
            }

            // Add context to MDC and proceed with method execution
            return joinPoint.proceed();
        } catch (Exception ex) {
            log.error("Error during API execution: {}", ex.getMessage(), ex);
            throw ex; // Re-throw the exception to ensure proper handling upstream
        } finally {
            // Clear MDC to prevent memory leaks
            MDC.clear();
        }
    }

}
