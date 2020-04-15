package com.kosinskyi.ecom.registry.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

  private static final String VOID_RETURN_TYPE = "void";

//  @Around("com.kosinskyi.ecom.registry.logging.PointcutExpressionsHolder.controllerLayer() || "
//      + "com.kosinskyi.ecom.registry.logging.PointcutExpressionsHolder.repositoryLayer() ||"
//      + "com.kosinskyi.ecom.registry.logging.PointcutExpressionsHolder.serviceLayer()")
  public Object logAroundMethodExecution(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
    MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
    String methodName = signature.toShortString();
    String returnType = signature.getReturnType().getName();
    Object[] args = proceedingJoinPoint.getArgs();
    logBeforeMethodExecution(args, methodName);
    return proceedAndLogAfterMethodExecution(proceedingJoinPoint, args, methodName, returnType);
  }

  private void logBeforeMethodExecution(Object[] args, String methodName) {
    String argsString = Arrays.stream(args)
        .filter(Objects::nonNull)
        .map(arg -> String.format("%s: {%s}", arg.getClass(), arg.toString()))
        .reduce((arg1, arg2) -> String.format("%s; %s", arg1, arg2))
        .orElse("");
    log.info(String.format("%s method execution started with arguments [%s]", methodName, argsString));
  }

  private Object proceedAndLogAfterMethodExecution(
      ProceedingJoinPoint joinPoint,
      Object[] args,
      String methodName,
      String returnType) throws Throwable {
    Object result = joinPoint.proceed(args);
    logResultByReturnType(returnType, methodName, result);
    return result;
  }

  private void logResultByReturnType(String returnType, String methodName, Object result) {
    if (VOID_RETURN_TYPE.equals(returnType)) {
      log.info(String.format("%s method execution finished returning void", methodName));
    } else {
      log.info(String.format("%s method execution finished returning [%s: {%s}]",
          methodName, getResultClass(result), getResultString(result)));
    }
  }

  private Class getResultClass(Object result) {
    return result == null ? null : result.getClass();
  }

  private String getResultString(Object result) {
    return result == null ? null : result.toString();
  }

}