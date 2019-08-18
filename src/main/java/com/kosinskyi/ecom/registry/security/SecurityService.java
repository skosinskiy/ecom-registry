package com.kosinskyi.ecom.registry.security;

import com.kosinskyi.ecom.registry.dto.request.LoginRequest;
import com.kosinskyi.ecom.registry.dto.request.RefreshRequest;
import com.kosinskyi.ecom.registry.dto.response.LoginResponse;
import com.kosinskyi.ecom.registry.entity.Permission;
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
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    Authentication authentication = authenticateUser(loginRequest.getEmail(), loginRequest.getPassword());
    SecurityContextHolder.getContext().setAuthentication(authentication);
    User user = (User) authentication.getPrincipal();
    Long userId = user.getId();
    String jwtAccessToken = generateAccessToken(user);
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

  private Authentication authenticateUser(String username, String password) {
    return authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(username, password)
    );
  }

  public String generateAccessToken(User user) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + jwtAccessTokenExpirationInMs);

    return Jwts.builder()
        .setSubject(Long.toString(user.getId()))
        .setIssuedAt(now)
        .claim("email", user.getEmail())
        .claim("permissions", user.getPermissions())
        .claim("isAccountNonExpired", user.isAccountNonExpired())
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
    boolean isAccountNonExpired = Boolean.parseBoolean(claims.get("isAccountNonExpired").toString()) ;
    String userEmail = String.valueOf(claims.get("email"));
    List<Permission> userPermissions = ((List<String>) claims.get("permissions"))
        .stream().map(Permission::valueOf).collect(Collectors.toList());
    UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
        .username(userEmail)
        .password("")
        .authorities(userPermissions)
        .accountExpired(!isAccountNonExpired)
        .build();
    UsernamePasswordAuthenticationToken authentication =
        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

  public LoginResponse refreshToken(RefreshRequest refreshRequest) {
    String jwtRefreshToken = refreshRequest.getJwtRefreshToken();
    User user = userService.findUserByRefreshToken(jwtRefreshToken);
    if (!user.isAccountNonExpired()) {
      throw new AccountExpiredException("User account is expired");
    }
    if (user.getJwtRefreshTokenExpireDate().getTime() > System.currentTimeMillis()) {
      String newJwtRefreshToken = generateRefreshToken();
      String newJwtAccessToken = generateAccessToken(user);
      long jwtRefreshTokenExpireTimeInMs = System.currentTimeMillis() + jwtRefreshTokenExpirationInMs;
      userService.setRefreshToken(user.getId(), newJwtRefreshToken, jwtRefreshTokenExpireTimeInMs);
      return getLoginResponse(newJwtAccessToken, newJwtRefreshToken, jwtRefreshTokenExpireTimeInMs);
    } else {
      throw new ActionForbiddenException(String.format("Refresh token %s is expired", jwtRefreshToken));
    }
  }
}