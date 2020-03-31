package com.kosinskyi.ecom.registry.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kosinskyi.ecom.registry.dto.response.error.ErrorResponse;
import com.kosinskyi.ecom.registry.error.exception.AuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.kosinskyi.ecom.registry.security.SecurityService.AUTHORIZATION_HEADER;

@ControllerAdvice
@Slf4j
public class GenericExceptionHandler {

  private ObjectMapper objectMapper;

  @Autowired
  public GenericExceptionHandler(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @ExceptionHandler
  public ResponseEntity<ErrorResponse> handleException(Exception originalExc, HttpServletRequest request) {
    log.error(originalExc.getMessage(), originalExc);
    Exception exception = mapOriginalException(originalExc, request);
    HttpStatus httpStatus = getHttpStatusForException(exception);
    ErrorResponse errorResponse = new ErrorResponse(exception, request, httpStatus);
    return new ResponseEntity<>(errorResponse, httpStatus);
  }

  public void handleAuthException(Exception exc, HttpServletRequest req, HttpServletResponse res) throws IOException {
    log.error(exc.getMessage(), exc);
    HttpStatus httpStatus = getHttpStatusForException(exc);
    ErrorResponse errorResponse = new ErrorResponse(exc, req, httpStatus);
    res.setStatus(httpStatus.value());
    res.getWriter().write(objectMapper.writeValueAsString(errorResponse));
  }

  private Exception mapOriginalException(Exception exc, HttpServletRequest request) {
    if (exc instanceof AccessDeniedException && StringUtils.isEmpty(request.getHeader(AUTHORIZATION_HEADER))) {
      return new AuthenticationException("Full authentication is required");
    }
    return exc;
  }

  private HttpStatus getHttpStatusForException(Exception exc) {
    if (exc instanceof AuthenticationException || exc instanceof BadCredentialsException) {
      return HttpStatus.UNAUTHORIZED;
    }
    if (exc instanceof AccessDeniedException) {
      return HttpStatus.FORBIDDEN;
    }
    return HttpStatus.INTERNAL_SERVER_ERROR;
  }

}