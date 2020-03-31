package com.kosinskyi.ecom.registry.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kosinskyi.ecom.registry.dto.response.error.ErrorResponse;
import com.kosinskyi.ecom.registry.error.GenericExceptionHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
public class GenericExceptionHandlerTest {

  @Autowired
  private GenericExceptionHandler genericExceptionHandler;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  public void handleAuthenticationExceptionTest() throws IOException {
    String exceptionMessage = "Bad credentials";
    String servletPath = "servletPath";
    HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
    HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
    PrintWriter writer = mock(PrintWriter.class);
    AuthenticationException exception = new BadCredentialsException(exceptionMessage);

    when(httpServletRequest.getServletPath()).thenReturn(servletPath);
    when(httpServletResponse.getWriter()).thenReturn(writer);

    genericExceptionHandler.handleAuthException(exception, httpServletRequest, httpServletResponse);

    ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
    verify(writer, times(1)).write(captor.capture());
    ErrorResponse capturedErrorResponse = objectMapper.readValue(captor.getValue(), ErrorResponse.class);

    verify(httpServletResponse, times(1)).setStatus(HttpStatus.UNAUTHORIZED.value());
    assertNotNull(capturedErrorResponse.getTimeStamp());
    assertEquals(exceptionMessage, capturedErrorResponse.getMessage());
    assertEquals(HttpStatus.UNAUTHORIZED.name(), capturedErrorResponse.getError());
    assertEquals(HttpStatus.UNAUTHORIZED.value(), capturedErrorResponse.getStatus());
    assertEquals(servletPath, capturedErrorResponse.getPath());
  }

  @Test
  public void handleAccessDeniedExceptionTest() {
    String exceptionMessage = "Full authentication is required";
    String servletPath = "servletPath";
    HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
    AccessDeniedException exception = new AccessDeniedException(exceptionMessage);

    when(httpServletRequest.getServletPath()).thenReturn(servletPath);

    ErrorResponse errorResponse = genericExceptionHandler.handleException(exception, httpServletRequest).getBody();

    assertTrue(Objects.nonNull(errorResponse));
    assertNotNull(errorResponse.getTimeStamp());
    assertEquals(exceptionMessage, errorResponse.getMessage());
    assertEquals(HttpStatus.UNAUTHORIZED.name(), errorResponse.getError());
    assertEquals(HttpStatus.UNAUTHORIZED.value(), errorResponse.getStatus());
    assertEquals(servletPath, errorResponse.getPath());
  }

  @Test
  public void handleRuntimeExceptionTest() {
    String exceptionMessage = "exceptionMessage";
    String servletPath = "servletPath";
    HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
    RuntimeException exception = new RuntimeException(exceptionMessage);

    when(httpServletRequest.getServletPath()).thenReturn(servletPath);

    ErrorResponse errorResponse = genericExceptionHandler.handleException(exception, httpServletRequest).getBody();

    assertTrue(Objects.nonNull(errorResponse));
    assertNotNull(errorResponse.getTimeStamp());
    assertEquals(exceptionMessage, errorResponse.getMessage());
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.name(), errorResponse.getError());
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorResponse.getStatus());
    assertEquals(servletPath, errorResponse.getPath());
  }
}