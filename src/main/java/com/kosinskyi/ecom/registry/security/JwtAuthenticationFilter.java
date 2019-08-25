package com.kosinskyi.ecom.registry.security;

import io.jsonwebtoken.Claims;
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
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String bearerToken = request.getHeader(SecurityService.AUTHORIZATION_HEADER);
    if (headerHasJwtToken(bearerToken)) {
      Claims jwtClaims = securityService.getJwtClaims(getJwtFromRequest(bearerToken));
      securityService.setAuthenticationFromClaims(jwtClaims, request);
    }
    filterChain.doFilter(request, response);
  }

  private boolean headerHasJwtToken(String bearerToken) {
    return StringUtils.hasText(bearerToken) && bearerToken.startsWith(SecurityService.TOKEN_TYPE);
  }

  private String getJwtFromRequest(String bearerToken) {
      return bearerToken.substring(SecurityService.TOKEN_TYPE.length());
  }

}