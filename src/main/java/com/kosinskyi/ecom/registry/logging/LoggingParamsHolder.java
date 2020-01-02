package com.kosinskyi.ecom.registry.logging;

import lombok.Builder;
import lombok.Getter;
import org.slf4j.Logger;
import org.springframework.boot.logging.LogLevel;

@Builder
@Getter
public class LoggingParamsHolder {

  private Logger log;
  private String methodName;
  private String returnType;
  private LogLevel logLevel;
  private Object[] args;

}
