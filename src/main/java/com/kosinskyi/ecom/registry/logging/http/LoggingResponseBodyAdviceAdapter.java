package com.kosinskyi.ecom.registry.logging.http;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class LoggingResponseBodyAdviceAdapter implements ResponseBodyAdvice<Object> {

  private final HttpLoggingService httpLoggingService;

  @Autowired
  public LoggingResponseBodyAdviceAdapter(HttpLoggingService httpLoggingService) {
    this.httpLoggingService = httpLoggingService;
  }

  @Override
  public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> clazz) {
    return true;
  }

  @Override
  public Object beforeBodyWrite(
      Object object,
      MethodParameter methodParameter,
      MediaType mediaType,
      Class<? extends HttpMessageConverter<?>> clazz,
      ServerHttpRequest serverHttpRequest,
      ServerHttpResponse serverHttpResponse) {
    if (serverHttpResponse instanceof ServletServerHttpResponse) {
      httpLoggingService.logResponse(((ServletServerHttpResponse) serverHttpResponse).getServletResponse(), object);
    }
    return object;
  }
}