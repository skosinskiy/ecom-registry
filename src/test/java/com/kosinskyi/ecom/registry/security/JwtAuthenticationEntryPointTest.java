package com.kosinskyi.ecom.registry.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kosinskyi.ecom.registry.dto.response.ErrorResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@SpringBootTest
@RunWith(SpringRunner.class)
public class JwtAuthenticationEntryPointTest {

	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Autowired
  private ObjectMapper objectMapper;

	@Test
	public void commenceTest() throws IOException {
    String errorMessage = "Bad credentials";
    String path = "/";
    BadCredentialsException badCredentialsException = new BadCredentialsException(errorMessage);
    HttpServletRequest mockRequest = mock(HttpServletRequest.class);
    HttpServletResponse mockResponse = mock(HttpServletResponse.class);
    PrintWriter mockWriter = mock(PrintWriter.class);

    when(mockRequest.getServletPath()).thenReturn(path);
    when(mockResponse.getWriter()).thenReturn(mockWriter);

    jwtAuthenticationEntryPoint.commence(mockRequest, mockResponse, badCredentialsException);

    ArgumentCaptor captor = ArgumentCaptor.forClass(String.class);
    verify(mockWriter, times(1))
        .write((String) captor.capture());
    ErrorResponse captureErrorResponse =
        objectMapper.readValue((String) captor.getValue(), ErrorResponse.class);

    assertEquals(errorMessage, captureErrorResponse.getMessage());
    assertEquals(HttpStatus.UNAUTHORIZED.name(), captureErrorResponse.getError());
    assertEquals(HttpStatus.UNAUTHORIZED.value(), captureErrorResponse.getStatus());
    assertEquals(path, captureErrorResponse.getPath());
    assertNotNull(captureErrorResponse.getTimeStamp());

    verify(mockResponse, times(1))
        .sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.name());
	}
}