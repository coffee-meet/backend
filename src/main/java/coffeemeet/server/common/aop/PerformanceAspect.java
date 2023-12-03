package coffeemeet.server.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class PerformanceAspect {

  @Pointcut("@annotation(coffeemeet.server.common.annotation.PerformanceMeasurement)")
  public void performanceMeasurementMethod() {
  }

  @Around("performanceMeasurementMethod()")
  public Object measureTime(ProceedingJoinPoint joinPoint) throws Throwable {
    long startTime = System.currentTimeMillis();
    try {
      return joinPoint.proceed();
    } finally {
      long endTime = System.currentTimeMillis();
      long executionTime = endTime - startTime;
      log.info(
          joinPoint.getSignature().toShortString() + " running time = " + executionTime + "ms");
    }
  }

}
