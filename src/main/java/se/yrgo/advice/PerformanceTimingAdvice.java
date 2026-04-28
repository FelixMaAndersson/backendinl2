package se.yrgo.advice;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class PerformanceTimingAdvice {

    @Around("execution(* se.yrgo.services.*.*(..)) || execution(* se.yrgo.dataaccess.*.*(..))")
    public Object performTimingMeasurement(ProceedingJoinPoint pjp) throws Throwable {

        Long start = System.nanoTime();

        Object result = pjp.proceed();

        Long end = System.nanoTime();
        double time = (end - start) / 1_000_000.0;

        String methodName = pjp.getSignature().getName();
        String className = pjp.getSignature().getDeclaringTypeName();

        System.out.println("Time taken for the method " + methodName + " from the class " + className + " took " + time + "ms");

        return result;
    }
}