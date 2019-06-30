package com.kosinskyi.ecom.registry.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(
      HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse,
      AuthenticationException exc) throws IOException {
    log.error("Responding with unauthorized error. Message - {}", exc.getMessage());
    httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, exc.getMessage());
  }
}
