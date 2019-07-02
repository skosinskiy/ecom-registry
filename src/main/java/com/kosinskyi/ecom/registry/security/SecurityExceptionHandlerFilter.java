package com.kosinskyi.ecom.registry.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kosinskyi.ecom.registry.dto.response.error.UnauthorizedErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class SecurityExceptionHandlerFilter extends OncePerRequestFilter {

  private ObjectMapper objectMapper;

  @Autowired
  public SecurityExceptionHandlerFilter(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    try {
      filterChain.doFilter(request, response);
    } catch (RuntimeException exc) {
      UnauthorizedErrorResponse unauthorizedErrorResponse = new UnauthorizedErrorResponse(exc, request);
      response.getWriter().write(objectMapper.writeValueAsString(unauthorizedErrorResponse));
      response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.name());
    }
  }
}