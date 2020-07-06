package com.kosinskyi.ecom.registry.config;

import com.kosinskyi.ecom.registry.logging.http.GetRequestHandlerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  private final GetRequestHandlerInterceptor getRequestHandlerInterceptor;

  @Autowired
  public WebMvcConfig(GetRequestHandlerInterceptor getRequestHandlerInterceptor) {
    this.getRequestHandlerInterceptor = getRequestHandlerInterceptor;
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(getRequestHandlerInterceptor);
  }
}