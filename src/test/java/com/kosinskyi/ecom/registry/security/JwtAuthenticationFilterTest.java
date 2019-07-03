package com.kosinskyi.ecom.registry.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class JwtAuthenticationFilterTest {

	@MockBean
	private SecurityService securityService;

	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	@Test
	public void doFilterInternalValidTokenTest() throws ServletException, IOException {
		String header = "Bearer Token";
		String token = "Token";
		HttpServletRequest mockRequest = mock(HttpServletRequest.class);
		HttpServletResponse mockResponse = mock(HttpServletResponse.class);
		FilterChain mockFilterChain = mock(FilterChain.class);

		when(mockRequest.getHeader("Authorization")).thenReturn(header);
		when(securityService.isTokenValid(token)).thenReturn(true);

		jwtAuthenticationFilter.doFilterInternal(mockRequest, mockResponse, mockFilterChain);

		verify(securityService, times(1)).setAuthenticationFromJwt(token, mockRequest);
		verify(mockFilterChain, times(1)).doFilter(mockRequest, mockResponse);
	}

	@Test
	public void doFilterInternalInvalidTokenTest() throws ServletException, IOException {
		String header = "Bearer Token";
		String token = "Token";
		HttpServletRequest mockRequest = mock(HttpServletRequest.class);
		HttpServletResponse mockResponse = mock(HttpServletResponse.class);
		FilterChain mockFilterChain = mock(FilterChain.class);

		when(mockRequest.getHeader("Authorization")).thenReturn(header);
		when(securityService.isTokenValid(token)).thenReturn(false);

		jwtAuthenticationFilter.doFilterInternal(mockRequest, mockResponse, mockFilterChain);

		verify(securityService, times(0)).setAuthenticationFromJwt(token, mockRequest);
		verify(mockFilterChain, times(1)).doFilter(mockRequest, mockResponse);
	}

	@Test
	public void doFilterInternalMissingTokenTest() throws ServletException, IOException {
		String header = null;
		HttpServletRequest mockRequest = mock(HttpServletRequest.class);
		HttpServletResponse mockResponse = mock(HttpServletResponse.class);
		FilterChain mockFilterChain = mock(FilterChain.class);

		when(mockRequest.getHeader("Authorization")).thenReturn(header);

		jwtAuthenticationFilter.doFilterInternal(mockRequest, mockResponse, mockFilterChain);

		verify(securityService, times(0)).setAuthenticationFromJwt(anyString(), eq(mockRequest));
		verify(mockFilterChain, times(1)).doFilter(mockRequest, mockResponse);
	}

	@Test
	public void doFilterInternalWrongTokenTest() throws ServletException, IOException {
		String header = "Invalid Token";
		HttpServletRequest mockRequest = mock(HttpServletRequest.class);
		HttpServletResponse mockResponse = mock(HttpServletResponse.class);
		FilterChain mockFilterChain = mock(FilterChain.class);

		when(mockRequest.getHeader("Authorization")).thenReturn(header);

		jwtAuthenticationFilter.doFilterInternal(mockRequest, mockResponse, mockFilterChain);

		verify(securityService, times(0)).setAuthenticationFromJwt(anyString(), eq(mockRequest));
		verify(mockFilterChain, times(1)).doFilter(mockRequest, mockResponse);
	}
}