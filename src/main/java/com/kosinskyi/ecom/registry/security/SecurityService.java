package com.kosinskyi.ecom.registry.security;

import com.kosinskyi.ecom.registry.dto.request.LoginRequest;
import com.kosinskyi.ecom.registry.dto.response.LoginResponse;
import com.kosinskyi.ecom.registry.dto.response.UserLoginResponse;
import com.kosinskyi.ecom.registry.entity.User;
import com.kosinskyi.ecom.registry.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
@Slf4j
public class SecurityService {

  public static final String TOKEN_TYPE = "Bearer";
  private AuthenticationManager authenticationManager;
  private UserService userService;

  @Value("${app.jwtSecret}")
  private String jwtSecret;

  @Value("${app.jwtExpirationInMs}")
  private int jwtExpirationInMs;

  @Autowired
  public SecurityService(@Lazy AuthenticationManager authenticationManager, UserService userService) {
    this.authenticationManager = authenticationManager;
    this.userService = userService;
  }

  public LoginResponse setAuthenticationAndGenerateJwt(LoginRequest loginRequest) {
    Authentication authentication = authenticateUser(loginRequest);
    SecurityContextHolder.getContext().setAuthentication(authentication);
    LoginResponse loginResponse = new LoginResponse();
    loginResponse.setJwt(generateToken(authentication));
    loginResponse.setTokenType(TOKEN_TYPE);
    User user = (User) authentication.getPrincipal();
    UserLoginResponse userLoginResponse = new UserLoginResponse();
    userLoginResponse.setRole("admin");
    loginResponse.setUser(userLoginResponse);
    return loginResponse;
  }

  private Authentication authenticateUser(LoginRequest loginRequest) {
    return authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
    );
  }

  public String generateToken(Authentication authentication) {
    User user = (User) authentication.getPrincipal();
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

    return Jwts.builder()
        .setSubject(Long.toString(user.getId()))
        .setIssuedAt(now)
        .setExpiration(expiryDate)
        .signWith(SignatureAlgorithm.HS512, jwtSecret)
        .compact();
  }

  public boolean isTokenValid(String jwt) {
    if (StringUtils.hasText(jwt)) {
      validateJwt(jwt);
      return true;
    }
    return false;
  }

  private void validateJwt(String jwt) {
    Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwt);
  }

  public void setAuthenticationFromJwt(String jwt, HttpServletRequest request) {
    Long userId = getUserIdFromJwt(jwt);
    UserDetails userDetails = userService.loadUserById(userId);
    UsernamePasswordAuthenticationToken authentication =
        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

  public Long getUserIdFromJwt(String token) {
    Claims claims = Jwts.parser()
        .setSigningKey(jwtSecret)
        .parseClaimsJws(token)
        .getBody();

    return Long.parseLong(claims.getSubject());
  }


}