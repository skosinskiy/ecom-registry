package com.kosinskyi.ecom.registry.filter;

import com.kosinskyi.ecom.registry.error.GenericExceptionHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = {"/*"})
@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {

  private final GenericExceptionHandler genericExceptionHandler;

  public ExceptionHandlerFilter(GenericExceptionHandler genericExceptionHandler) {
    this.genericExceptionHandler = genericExceptionHandler;
  }

  @Override
  public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    try {
      filterChain.doFilter(request, response);
    } catch (RuntimeException exc) {
      genericExceptionHandler.handleAuthException(exc, request, response);
    }
  }

}