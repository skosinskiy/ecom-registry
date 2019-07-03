package com.kosinskyi.ecom.registry.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kosinskyi.ecom.registry.dto.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
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

  @ExceptionHandler
  public void handleException(Exception exc, HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    log.error(exc.getMessage(), exc);
    HttpStatus httpStatus = getHttpStatusForException(exc);
    ErrorResponse internalErrorResponse = new ErrorResponse(exc, request, httpStatus);
    response.getWriter().write(objectMapper.writeValueAsString(internalErrorResponse));
    response.sendError(httpStatus.value(), httpStatus.name());
  }

  private HttpStatus getHttpStatusForException(Exception exc) {
    if (exc instanceof AuthenticationException) {
      return HttpStatus.UNAUTHORIZED;
    }
    if (exc instanceof AccessDeniedException) {
      return HttpStatus.FORBIDDEN;
    }
    return HttpStatus.INTERNAL_SERVER_ERROR;
  }

}