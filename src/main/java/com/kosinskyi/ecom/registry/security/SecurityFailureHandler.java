package com.kosinskyi.ecom.registry.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kosinskyi.ecom.registry.dto.response.AuthResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
public class SecurityFailureHandler implements AuthenticationFailureHandler {

  private ObjectMapper objectMapper;

  @Autowired
  public SecurityFailureHandler(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public void onAuthenticationFailure(
      HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse,
      AuthenticationException exc) throws IOException {
    AuthResult authResult = AuthResult.builder()
        .timestamp(new Date(System.currentTimeMillis()))
        .status(HttpStatus.UNAUTHORIZED.value())
        .message(HttpStatus.UNAUTHORIZED.getReasonPhrase())
        .build();
    httpServletResponse.getWriter().write(objectMapper.writeValueAsString(authResult));
    httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
  }
}
