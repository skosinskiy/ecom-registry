package com.kosinskyi.ecom.registry.security;

import com.kosinskyi.ecom.registry.filter.JwtAuthenticationFilter;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
public class JwtAuthenticationFilterTest {

  @Autowired
  private JwtAuthenticationFilter jwtAuthenticationFilter;

  @MockBean
  private SecurityService securityService;

  @Test
  public void doFilterInternalEmptyHeaderTest() throws ServletException, IOException {

    HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
    HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
    FilterChain filterChain = mock(FilterChain.class);

    when(httpServletRequest.getHeader(SecurityService.AUTHORIZATION_HEADER)).thenReturn(null);

    jwtAuthenticationFilter.doFilterInternal(httpServletRequest, httpServletResponse, filterChain);

    verify(securityService, times(0))
        .setAuthenticationFromJwt(anyString(), any(HttpServletRequest.class));
    verify(filterChain, times(1)).doFilter(httpServletRequest, httpServletResponse);
  }

  @Test
  public void doFilterInternalWrongAuthTokenTest() throws ServletException, IOException {

    HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
    HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
    FilterChain filterChain = mock(FilterChain.class);

    when(httpServletRequest.getHeader(SecurityService.AUTHORIZATION_HEADER)).thenReturn("test");

    jwtAuthenticationFilter.doFilterInternal(httpServletRequest, httpServletResponse, filterChain);

    verify(securityService, times(0))
        .setAuthenticationFromJwt(anyString(), any(HttpServletRequest.class));
    verify(filterChain, times(1)).doFilter(httpServletRequest, httpServletResponse);
  }

  @Test
  public void doFilterInternalTest() throws ServletException, IOException {
    String jwt = "jwt";
    String authHeader = SecurityService.TOKEN_TYPE + jwt;
    HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
    HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
    FilterChain filterChain = mock(FilterChain.class);

    when(httpServletRequest.getHeader(SecurityService.AUTHORIZATION_HEADER)).thenReturn(authHeader);

    jwtAuthenticationFilter.doFilterInternal(httpServletRequest, httpServletResponse, filterChain);

    verify(securityService, times(1))
        .setAuthenticationFromJwt(jwt, httpServletRequest);
    verify(filterChain, times(1)).doFilter(httpServletRequest, httpServletResponse);
  }

}