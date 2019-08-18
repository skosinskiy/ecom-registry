package com.kosinskyi.ecom.registry.security;

import com.kosinskyi.ecom.registry.dto.request.LoginRequest;
import com.kosinskyi.ecom.registry.dto.request.RefreshRequest;
import com.kosinskyi.ecom.registry.dto.response.LoginResponse;
import com.kosinskyi.ecom.registry.entity.User;
import com.kosinskyi.ecom.registry.exception.ActionForbiddenException;
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

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
public class SecurityService {

  static final String AUTHORIZATION_HEADER = "Authorization";
  static final String TOKEN_TYPE = "Bearer ";
  private AuthenticationManager authenticationManager;
  private UserService userService;

  @Value("${app.jwtSecret}")
  private String jwtSecret;

  @Value("${app.jwtAccessTokenExpirationInMs}")
  private int jwtAccessTokenExpirationInMs;

  @Value("${app.jwtRefreshTokenExpirationInMs}")
  private int jwtRefreshTokenExpirationInMs;

  @Autowired
  public SecurityService(@Lazy AuthenticationManager authenticationManager, UserService userService) {
    this.authenticationManager = authenticationManager;
    this.userService = userService;
  }

  public LoginResponse setAuthenticationAndGenerateJwt(LoginRequest loginRequest) {
    Authentication authentication = authenticateUser(loginRequest);
    SecurityContextHolder.getContext().setAuthentication(authentication);
    User user = (User) authentication.getPrincipal();
    Long userId = user.getId();
    String jwtAccessToken = generateAccessToken(userId);
    String jwtRefreshToken = generateRefreshToken();
    long jwtRefreshTokenExpireTimeInMs = System.currentTimeMillis() + jwtRefreshTokenExpirationInMs;
    userService.setRefreshToken(userId, jwtRefreshToken, jwtRefreshTokenExpireTimeInMs);
    return getLoginResponse(jwtAccessToken, jwtRefreshToken, jwtRefreshTokenExpireTimeInMs);
  }

  private LoginResponse getLoginResponse(
      String jwtAccessToken, String jwtRefreshToken, long jwtRefreshTokenExpireTimeInMs) {
    LoginResponse loginResponse = new LoginResponse();
    loginResponse.setJwtAccessToken(jwtAccessToken);
    loginResponse.setJwtRefreshToken(jwtRefreshToken);
    loginResponse.setJwtRefreshTokenExpireDate(jwtRefreshTokenExpireTimeInMs);
    loginResponse.setTokenType(TOKEN_TYPE);
    return loginResponse;
  }

  private Authentication authenticateUser(LoginRequest loginRequest) {
    return authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
    );
  }

  public String generateAccessToken(Long userId) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + jwtAccessTokenExpirationInMs);

    return Jwts.builder()
        .setSubject(Long.toString(userId))
        .setIssuedAt(now)
        .setExpiration(expiryDate)
        .signWith(SignatureAlgorithm.HS512, jwtSecret)
        .compact();
  }

  public String generateRefreshToken() {
    return UUID.randomUUID().toString();
  }

  public Claims getJwtClaims(String jwt) {
    return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwt).getBody();
  }

  public void setAuthenticationFromClaims(Claims claims, HttpServletRequest request) {
    Long userId = Long.parseLong(claims.getSubject());
    UserDetails userDetails = userService.loadUserById(userId);
    UsernamePasswordAuthenticationToken authentication =
        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

  public LoginResponse refreshToken(RefreshRequest refreshRequest) {
    String jwtRefreshToken = refreshRequest.getJwtRefreshToken();
    User user = userService.findUserByRefreshToken(jwtRefreshToken);
    if (user.getJwtRefreshTokenExpireDate().getTime() > System.currentTimeMillis()) {
      String newJwtRefreshToken = generateRefreshToken();
      String newJwtAccessToken = generateAccessToken(user.getId());
      long jwtRefreshTokenExpireTimeInMs = System.currentTimeMillis() + jwtRefreshTokenExpirationInMs;
      userService.setRefreshToken(user.getId(), newJwtRefreshToken, jwtRefreshTokenExpireTimeInMs);
      return getLoginResponse(newJwtAccessToken, newJwtRefreshToken, jwtRefreshTokenExpireTimeInMs);
    } else {
      throw new ActionForbiddenException(String.format("Refresh token %s is expired", jwtRefreshToken));
    }
  }
}