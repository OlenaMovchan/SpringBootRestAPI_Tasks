package com.example.tasknewspring.aspect;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Around("(@annotation(org.springframework.web.bind.annotation.GetMapping) || @annotation(org.springframework.web.bind.annotation.PostMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PutMapping) || @annotation(org.springframework.web.bind.annotation.DeleteMapping)) " +
            "&& @annotation(com.example.tasknewspring.aspect.LogExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

        String methodName = joinPoint.getSignature().getName();

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        log.info("Starting execution method {}", methodName);

        Object result = joinPoint.proceed();

        log.info("Finished execution method {}, time {}", methodName, stopWatch.getTime(TimeUnit.MILLISECONDS));

        return result;
    }
}
