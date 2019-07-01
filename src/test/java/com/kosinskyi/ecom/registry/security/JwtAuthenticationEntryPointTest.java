package com.kosinskyi.ecom.registry.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@SpringBootTest
@RunWith(SpringRunner.class)
public class JwtAuthenticationEntryPointTest {

	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Test
	public void commenceTest() throws IOException {
		BadCredentialsException badCredentialsException = new BadCredentialsException("Bad credentials");
		HttpServletRequest mockRequest = mock(HttpServletRequest.class);
		HttpServletResponse mockResponse = mock(HttpServletResponse.class);

		jwtAuthenticationEntryPoint.commence(mockRequest, mockResponse, badCredentialsException);

		verify(mockResponse, times(1))
				.sendError(HttpServletResponse.SC_UNAUTHORIZED, badCredentialsException.getMessage());
	}
}