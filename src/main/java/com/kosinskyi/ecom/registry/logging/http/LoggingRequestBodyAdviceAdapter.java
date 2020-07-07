package com.kosinskyi.ecom.registry.logging.http;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.lang.reflect.Type;

@ControllerAdvice
public class LoggingRequestBodyAdviceAdapter extends RequestBodyAdviceAdapter {

  private final HttpLoggingService httpLoggingService;

  @Autowired
  public LoggingRequestBodyAdviceAdapter(HttpLoggingService httpLoggingService) {
    this.httpLoggingService = httpLoggingService;
  }

  @Override
  public boolean supports(MethodParameter parameter, Type type, Class<? extends HttpMessageConverter<?>> clazz) {
    return true;
  }

  @Override
  public Object afterBodyRead(
      Object body,
      HttpInputMessage inputMessage,
      MethodParameter parameter,
      Type targetType,
      Class<? extends HttpMessageConverter<?>> converterType) {
    httpLoggingService.logRequest(body);
    return super.afterBodyRead(body, inputMessage, parameter, targetType, converterType);
  }
}