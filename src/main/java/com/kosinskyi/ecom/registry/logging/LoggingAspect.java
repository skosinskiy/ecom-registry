package com.kosinskyi.ecom.registry.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Aspect
@Component
public class LoggingAspect {

  @Pointcut("within(@com.kosinskyi.ecom.registry.logging.Logging *)")
  public void beanAnnotatedWithLogging() {}

  @Pointcut("execution(@com.kosinskyi.ecom.registry.logging.IgnoreLogging * *(..))")
  public void methodAnnotatedWithIgnoreLogging() {}

  private static final String VOID_RETURN_TYPE = "void";
  private static final String DEFAULT_CONTENT_TYPE = "UNKNOWN";

  @Around("beanAnnotatedWithLogging() && !methodAnnotatedWithIgnoreLogging()")
  public Object logAroundMethodExecution(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
    LoggingParamsHolder loggingParams = getLoggingParams(proceedingJoinPoint);
    logBeforeMethodExecution(loggingParams);
    Object result = proceedingJoinPoint.proceed(loggingParams.getArgs());
    logResultByReturnType(result, loggingParams);
    return result;
  }

  private LoggingParamsHolder getLoggingParams(ProceedingJoinPoint proceedingJoinPoint) {
    Class<?> targetClass = proceedingJoinPoint.getTarget().getClass();
    Logging annotation = targetClass.getAnnotation(Logging.class);
    MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
    return LoggingParamsHolder
        .builder()
        .methodName(signature.getName())
        .returnType(signature.getReturnType().getName())
        .logLevel(annotation == null ? LogLevel.INFO : annotation.level())
        .log(LoggerFactory.getLogger(targetClass))
        .args(proceedingJoinPoint.getArgs())
        .build();
  }

  private void logWithLevel(String message, LoggingParamsHolder loggingParamsHolder) {
    Logger log = loggingParamsHolder.getLog();
    switch (loggingParamsHolder.getLogLevel()) {
      case TRACE:
        log.trace(message);
        return;
      case DEBUG:
        log.debug(message);
        return;
      case INFO:
        log.info(message);
        return;
      case WARN:
        log.warn(message);
        return;
      case ERROR:
        log.error(message);
        return;
      default:
        log.info(message);
    }
  }

  private void logBeforeMethodExecution(LoggingParamsHolder loggingParams) {
    String stringToLog =
        Arrays.stream(loggingParams.getArgs())
            .map(this::getArgAsString)
            .collect(Collectors.joining("; "));
    logWithLevel(String.format("IN: %s(%s)", loggingParams.getMethodName(), stringToLog), loggingParams);
  }

  private String getArgAsString(Object arg) {
    if (arg instanceof Collection) {
      return getCollectionLogString(arg);
    }
    return String.valueOf(arg);
  }

  private String getCollectionLogString(Object collectionObject) {
    Collection<?> collection = (Collection<?>) collectionObject;
    return String.format(
        "%s containing %d {%s} instances", collectionObject.getClass(), collection.size(), getContentType(collection));
  }

  private String getContentType(Collection<?> collection) {
    if (!collection.isEmpty()) {
      return collection.iterator().next().getClass().getName();
    }
    return DEFAULT_CONTENT_TYPE;
  }

  private void logResultByReturnType(Object result, LoggingParamsHolder loggingParams) {
    logWithLevel(String.format("OUT: %s: %s",
        loggingParams.getMethodName(), getResultString(result, loggingParams)), loggingParams);
  }

  private String getResultString(Object result, LoggingParamsHolder loggingParams) {
    if (VOID_RETURN_TYPE.equals(loggingParams.getReturnType())) {
      return VOID_RETURN_TYPE;
    }
    if (result instanceof Collection) {
      return getCollectionLogString(result);
    }
    if (result instanceof ResponseEntity) {
      ResponseEntity<?> responseEntity = (ResponseEntity<?>) result;
      Object responseBody = responseEntity.getBody();
      if (responseBody instanceof Collection) {
        return getResponseEntityWithCollectionLogString(responseEntity, getCollectionLogString(responseBody));
      }
    }
    return String.valueOf(result);
  }

  private String getResponseEntityWithCollectionLogString(ResponseEntity<?> responseEntity, String collectionLogString) {
    StringBuilder builder = new StringBuilder("<");
    builder.append(responseEntity.getStatusCode());
    builder.append(',');
    builder.append(collectionLogString);
    builder.append(',');
    HttpHeaders headers = responseEntity.getHeaders();
    builder.append(headers);
    builder.append('>');
    return builder.toString();
  }

}

