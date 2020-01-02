package com.kosinskyi.ecom.registry.logging.http;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class GetRequestHandlerInterceptor implements HandlerInterceptor {

  private HttpLoggingService httpLoggingService;

  @Autowired
  public GetRequestHandlerInterceptor(HttpLoggingService httpLoggingService) {
    this.httpLoggingService = httpLoggingService;
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
    if (request.getMethod().equals(HttpMethod.GET.name())) {
      httpLoggingService.logRequest(null);
    }
    return true;
  }

}