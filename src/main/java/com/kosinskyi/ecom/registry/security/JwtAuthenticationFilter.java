package com.kosinskyi.ecom.registry.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private SecurityService securityService;

  @Autowired
  public JwtAuthenticationFilter(SecurityService securityService) {
    this.securityService = securityService;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    String jwt = getJwtFromRequest(request);
    if (securityService.isTokenValid(jwt)) {
      securityService.setAuthenticationFromJwt(jwt, request);
    }
    filterChain.doFilter(request, response);
  }

  private String getJwtFromRequest(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(SecurityService.TOKEN_TYPE)) {
      return bearerToken.substring(7);
    }
    return null;
  }

}
