package com.kosinskyi.ecom.registry.filter;

import com.kosinskyi.ecom.registry.security.SecurityService;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = {"/*"})
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private SecurityService securityService;

  @Autowired
  public JwtAuthenticationFilter(SecurityService securityService) {
    this.securityService = securityService;
  }

  @Override
  public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String bearerToken = request.getHeader(SecurityService.AUTHORIZATION_HEADER);
    if (headerHasJwtToken(bearerToken)) {
      securityService.setAuthenticationFromJwt(getJwtFromRequest(bearerToken), request);
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