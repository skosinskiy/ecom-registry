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
import java.util.stream.Collectors;

@Service
@Slf4j
public class SecurityService {

  static final String AUTHORIZATION_HEADER = "Authorization";
  static final String TOKEN_TYPE = "Bearer ";
  private static final String EMAIL_CLAIM = "email";
  private static final String PERMISSIONS_CLAIM = "permissions";
  private static final String IS_ACCOUNT_NON_EXPIRED_CLAIM = "isAccountNonExpired";
  private AuthenticationManager authenticationManager;
  private UserService userService;

  @Value("${app.jwtSecret}")
  private String jwtSecret;

  @Value("${app.jwtAccessTokenExpirationInMs}")
  private Long jwtAccessTokenExpirationInMs;

  @Value("${app.jwtRefreshTokenExpirationInMs}")
  private Long jwtRefreshTokenExpirationInMs;

  @Autowired
  public SecurityService(@Lazy AuthenticationManager authenticationManager, UserService userService) {
    this.authenticationManager = authenticationManager;
    this.userService = userService;
  }

  public LoginResponse setAuthenticationAndGenerateJwt(LoginRequest loginRequest) {
    Authentication authentication = authenticateUser(loginRequest.getEmail(), loginRequest.getPassword());
    SecurityContextHolder.getContext().setAuthentication(authentication);
    User user = (User) authentication.getPrincipal();
    User userWithRefreshToken = userService.setRefreshToken(user, jwtRefreshTokenExpirationInMs);
    return getLoginResponse(userWithRefreshToken);
  }

  private Authentication authenticateUser(String username, String password) {
    return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
  }

  private LoginResponse getLoginResponse(
      User userWithRefreshToken) {
    LoginResponse loginResponse = new LoginResponse();
    loginResponse.setJwtAccessToken(generateAccessToken(userWithRefreshToken));
    loginResponse.setJwtRefreshToken(userWithRefreshToken.getJwtRefreshToken());
    loginResponse.setJwtRefreshTokenExpireDate(userWithRefreshToken.getJwtRefreshTokenExpireDate().getTime());
    loginResponse.setTokenType(TOKEN_TYPE);
    return loginResponse;
  }

  private String generateAccessToken(User user) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + jwtAccessTokenExpirationInMs);

    return Jwts.builder()
        .setSubject(Long.toString(user.getId()))
        .claim(EMAIL_CLAIM, user.getEmail())
        .claim(PERMISSIONS_CLAIM, user.getPermissions())
        .claim(IS_ACCOUNT_NON_EXPIRED_CLAIM, user.isAccountNonExpired())
        .setIssuedAt(now)
        .setExpiration(expiryDate)
        .signWith(SignatureAlgorithm.HS512, jwtSecret)
        .compact();
  }

  public Claims getJwtClaims(String jwt) {
    return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwt).getBody();
  }

  public void setAuthenticationFromClaims(Claims claims, HttpServletRequest request) {
    UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
        .username(String.valueOf(claims.get(EMAIL_CLAIM)))
        .password("")
        .authorities(getUserPermissionsFromClaim(claims))
        .accountExpired(!Boolean.parseBoolean(claims.get(IS_ACCOUNT_NON_EXPIRED_CLAIM).toString()))
        .build();
    UsernamePasswordAuthenticationToken authentication =
        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

  @SuppressWarnings("ALL")
  private List<Permission> getUserPermissionsFromClaim(Claims claims) {
    return ((List<String>) claims.get(PERMISSIONS_CLAIM))
        .stream()
        .map(Permission::valueOf)
        .collect(Collectors.toList());
  }

  public LoginResponse refreshToken(RefreshRequest refreshRequest) {
    User user = userService.findUserByRefreshToken(refreshRequest.getJwtRefreshToken());
    checkIsUserExpired(user);
    checkIsJwtRefreshTokenExpired(user);
    User userWithRefreshToken = userService.setRefreshToken(user, jwtRefreshTokenExpirationInMs);
    return getLoginResponse(userWithRefreshToken);
  }

  private void checkIsUserExpired(User user) {
    if (!user.isAccountNonExpired()) {
      throw new AccountExpiredException("User account is expired");
    }
  }

  private void checkIsJwtRefreshTokenExpired(User user) {
    if (user.getJwtRefreshTokenExpireDate().getTime() < System.currentTimeMillis()) {
      throw new ActionForbiddenException(String.format("Refresh token %s is expired", user.getJwtRefreshToken()));
    }
  }
}