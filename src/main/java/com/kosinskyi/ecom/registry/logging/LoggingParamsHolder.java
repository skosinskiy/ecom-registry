package com.kosinskyi.ecom.registry.logging;

import lombok.Builder;
import lombok.Getter;
import org.slf4j.Logger;
import org.springframework.boot.logging.LogLevel;

@Builder
@Getter
public class LoggingParamsHolder {

  private final Logger log;
  private final String methodName;
  private final String returnType;
  private final LogLevel logLevel;
  private final Object[] args;

}
