package com.example.todoproject.aop.logtrace;

import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@RequiredArgsConstructor
@Component
public class LogAspect {

    private final LogTracer logTracer;

    @Pointcut("execution(* com.example.todoproject..*Controller.*(..)) || execution(* com.example.todoproject..*Service.*(..)) || execution(* com.example.todoproject..*Repository.*(..))")
    public void everyRequest() { }

    @Pointcut("@annotation(com.example.todoproject.aop.logtrace.NoLogging)")
    public void noLogging() { }

    @Around("everyRequest() && !noLogging()")
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
        TraceStatus status = null;
        boolean hasException = false;
        try {
            status = logTracer.begin(" Method Signature : " + joinPoint.getSignature().toString().replace("com.example.todoproject.", ""),
                    Arrays.deepToString(joinPoint.getArgs()));
            return joinPoint.proceed();
        } catch (Exception ex) {
            logTracer.handleException(status, ex);
            hasException = true;
            throw ex;
        } finally {
            if(!hasException) logTracer.end(status);
        }
    }

}
