package com.kosinskyi.ecom.registry.security;

import com.kosinskyi.ecom.registry.dto.request.LoginRequest;
import com.kosinskyi.ecom.registry.dto.response.LoginResponse;
import com.kosinskyi.ecom.registry.entity.Permission;
import com.kosinskyi.ecom.registry.entity.User;
import com.kosinskyi.ecom.registry.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SecurityServiceTest {

  @Autowired
  private SecurityService securityService;

  @MockBean
  private AuthenticationManager authenticationManager;

  @MockBean
  private UserService userService;

  @Value("${app.jwtRefreshTokenExpirationInMs}")
  private Long jwtRefreshTokenExpirationInMs;

  @Test
  public void setAuthenticationAndGenerateJwtTest() {
    String email = "email";
    String password = "password";
    LoginRequest loginRequest = new LoginRequest();
    loginRequest.setEmail(email);
    loginRequest.setPassword(password);
    Long userId = 1L;
    User user = new User();
    user.setId(userId);
    user.setEmail(email);

    String jwtRefreshToken = "jwtRefreshToken";
    List<Permission> permissions = new ArrayList<>();
    permissions.add(Permission.MANAGE_ACCOUNTS);
    permissions.add(Permission.MANAGE_REGISTRY);
    User userWithRefreshToken = new User();
    userWithRefreshToken.setId(userId);
    userWithRefreshToken.setEmail(email);
    userWithRefreshToken.setJwtRefreshToken(jwtRefreshToken);
    userWithRefreshToken.setJwtRefreshTokenExpireDate(new Date(jwtRefreshTokenExpirationInMs));
    userWithRefreshToken.setAccountExpireDate(new Date(System.currentTimeMillis() + 1000000));
    userWithRefreshToken.setPermissions(permissions);

    Authentication authentication = mock(Authentication.class);

    when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password)))
        .thenReturn(authentication);
    when(authentication.getPrincipal()).thenReturn(user);
    when(userService.setRefreshToken(user, jwtRefreshTokenExpirationInMs)).thenReturn(userWithRefreshToken);

    LoginResponse loginResponse = securityService.setAuthenticationAndGenerateJwt(loginRequest);

    Claims claims = Jwts
        .parser()
        .setSigningKey(securityService.getJwtSecret())
        .parseClaimsJws(loginResponse.getJwtAccessToken())
        .getBody();
    assertEquals(authentication, SecurityContextHolder.getContext().getAuthentication());
    assertEquals(jwtRefreshToken, loginResponse.getJwtRefreshToken());
    assertEquals(jwtRefreshTokenExpirationInMs, loginResponse.getJwtRefreshTokenExpireDate());
    assertEquals(SecurityService.TOKEN_TYPE, loginResponse.getTokenType());
    assertEquals(String.valueOf(userId), claims.getSubject());
    assertEquals(email, claims.get(SecurityService.EMAIL_CLAIM));
    assertTrue(Boolean.parseBoolean(claims.get(SecurityService.IS_ACCOUNT_NON_EXPIRED_CLAIM).toString()));
    assertEquals(permissions, ((List<String>) claims.get(SecurityService.PERMISSIONS_CLAIM))
        .stream()
        .map(Permission::valueOf)
        .collect(Collectors.toList()));
  }

  @Test
  public void setAuthenticationFromJwt() {

  }

  @Test
  public void refreshToken() {
  }
}