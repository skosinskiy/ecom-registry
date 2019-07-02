package com.kosinskyi.ecom.registry.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kosinskyi.ecom.registry.dto.response.error.UnauthorizedErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private ObjectMapper objectMapper;

  @Autowired
  public JwtAuthenticationEntryPoint(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public void commence(
      HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse,
      AuthenticationException exc) throws IOException {
    log.error("Responding with unauthorized error. Message - {}", exc.getMessage());
    UnauthorizedErrorResponse unauthorizedErrorResponse = new UnauthorizedErrorResponse(exc, httpServletRequest);
    httpServletResponse.getWriter().write(objectMapper.writeValueAsString(unauthorizedErrorResponse));
    httpServletResponse.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.name());
  }
}
