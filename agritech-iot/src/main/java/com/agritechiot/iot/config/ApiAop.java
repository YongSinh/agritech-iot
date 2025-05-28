package com.agritechiot.iot.config;

import com.agritechiot.iot.constant.GenConstant;
import com.agritechiot.iot.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Aspect
@Component
@Slf4j
public class ApiAop {

    @Around("execution(* com.agritechiot.iot.controller.*.*(..))")
    public Object flow(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            Object[] args = joinPoint.getArgs();

            // Handle CORRELATION_ID
            String correlationId = (args.length > 0 && args[0] != null)
                    ? args[0].toString()
                    : GenConstant.DEFAULT_CORRELATION_ID;
            MDC.put(GenConstant.CORRELATION_ID, correlationId);


            Object result = joinPoint.proceed();

            if (result instanceof Mono) {
                return ((Mono<?>) result)
                        .doOnNext(value -> log.info("TRACE_ID: [{}] - Returned value: {}", correlationId, JsonUtil.toJson(value)))
                        .doOnError(error -> log.error("TRACE_ID: [{}] - Error in reactive flow: {}", correlationId, error.getMessage(), error))
                        .doFinally(signalType -> MDC.clear());
            }

            return result;
        } catch (Exception ex) {
            log.error("Error during API execution: {}", ex.getMessage(), ex);
            throw ex;
        }
    }


}
