package com.kosinskyi.ecom.registry.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kosinskyi.ecom.registry.dto.response.error.InternalErrorResponse;
import com.kosinskyi.ecom.registry.dto.response.error.UnauthorizedErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
@Slf4j
public class GenericExceptionHandler {

  private ObjectMapper objectMapper;

  @Autowired
  public GenericExceptionHandler(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @ExceptionHandler({AuthenticationException.class})
  public void handleSecurityException(Exception exc, HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    log.error(exc.getMessage(), exc);
    UnauthorizedErrorResponse unauthorizedErrorResponse = new UnauthorizedErrorResponse(exc, request);
    response.getWriter().write(objectMapper.writeValueAsString(unauthorizedErrorResponse));
    response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.name());
  }

  @ExceptionHandler
  public void handleException(Exception exc, HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    log.error(exc.getMessage(), exc);
    InternalErrorResponse internalErrorResponse = new InternalErrorResponse(exc, request);
    response.getWriter().write(objectMapper.writeValueAsString(internalErrorResponse));
    response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.name());
  }

}