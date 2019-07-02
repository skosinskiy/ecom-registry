package com.kosinskyi.ecom.registry.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@SpringBootTest
@RunWith(SpringRunner.class)
public class JwtAuthenticationEntryPointTest {

	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Test
	public void commenceTest() throws IOException {
		String errorMessage = "Bad credentials";
		BadCredentialsException badCredentialsException = new BadCredentialsException(errorMessage);
		HttpServletRequest mockRequest = mock(HttpServletRequest.class);
		HttpServletResponse mockResponse = mock(HttpServletResponse.class);
		PrintWriter mockWriter = mock(PrintWriter.class);

		when(mockResponse.getWriter()).thenReturn(mockWriter);

		jwtAuthenticationEntryPoint.commence(mockRequest, mockResponse, badCredentialsException);

    verify(mockWriter, times(1))
        .write(anyString());

		verify(mockResponse, times(1))
				.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.name());
	}
}