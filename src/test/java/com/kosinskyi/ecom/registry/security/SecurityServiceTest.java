package com.kosinskyi.ecom.registry.security;

import com.kosinskyi.ecom.registry.dto.request.LoginRequest;
import com.kosinskyi.ecom.registry.dto.response.LoginResponse;
import com.kosinskyi.ecom.registry.entity.Permission;
import com.kosinskyi.ecom.registry.entity.User;
import com.kosinskyi.ecom.registry.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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

  @Value("${app.jwtExpirationInMs}")
  private int jwtExpirationInMs;

  @Value("${app.jwtSecret}")
  private String jwtSecret;

  @Rule
  public ExpectedException exception = ExpectedException.none();

  @Test
  public void setAuthenticationAndGenerateJwtTest() {
    Long id = 1L;
    String email = "stanislav.kosinski@gmail.com";
    String password = "admin";

    User user = new User();
    user.setId(id);

    LoginRequest loginRequest = new LoginRequest();
    loginRequest.setEmail(email);
    loginRequest.setPassword(password);

    Authentication authentication = mock(Authentication.class);
    SecurityContext securityContext = mock(SecurityContext.class);

    SecurityContextHolder.setContext(securityContext);

    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
    when(authentication.getPrincipal()).thenReturn(user);

    LoginResponse loginResponse = securityService.setAuthenticationAndGenerateJwt(loginRequest);

    ArgumentCaptor captor = ArgumentCaptor.forClass(UsernamePasswordAuthenticationToken.class);
    verify(securityContext, times(1)).setAuthentication(authentication);
    verify(authenticationManager, times(1))
        .authenticate((UsernamePasswordAuthenticationToken)captor.capture());
    UsernamePasswordAuthenticationToken usernameToken = (UsernamePasswordAuthenticationToken) captor.getValue();

    assertEquals(email, usernameToken.getPrincipal());
    assertEquals(password, usernameToken.getCredentials());
    assertEquals(SecurityService.TOKEN_TYPE, loginResponse.getTokenType());
    assertEquals(id, securityService.getUserIdFromJwt(loginResponse.getJwt()));
  }

  @Test
  public void generateTokenTest() {
    long id = 1L;
    User user = new User();
    user.setId(id);

    Authentication authentication = mock(Authentication.class);
    when(authentication.getPrincipal()).thenReturn(user);

    String jwt = securityService.generateToken(authentication);

    Claims claims = Jwts.parser()
        .setSigningKey(jwtSecret)
        .parseClaimsJws(jwt)
        .getBody();

    assertEquals(id, Long.parseLong(claims.getSubject()));
    assertEquals(jwtExpirationInMs, claims.getExpiration().getTime() - claims.getIssuedAt().getTime() );

  }

  @Test
  public void isTokenValidEmptyTokenTest() {
    String jwt = "";
    assertFalse(securityService.isTokenValid(jwt));
  }

  @Test
  public void isTokenValidInvalidTokenTest() {
    String jwt = "Invalid.token.test";
    exception.expect(MalformedJwtException.class);
    exception.expectMessage("Unable to read JSON value");

    securityService.isTokenValid(jwt);
  }

  @Test
  public void isTokenValidSuccessTest() {
    long id = 1L;
    User user = new User();
    user.setId(id);

    Authentication authentication = mock(Authentication.class);
    when(authentication.getPrincipal()).thenReturn(user);

    String jwt = securityService.generateToken(authentication);

    assertTrue(securityService.isTokenValid(jwt));
  }

  @Test
  public void setAuthenticationFromJwt() {
    Long id = 1L;

    List<Permission> permissions = new ArrayList<>();
    permissions.add(Permission.MANAGE_REGISTRY);

    User user = new User();
    user.setId(id);
    user.setPermissions(permissions);

    HttpServletRequest request = mock(HttpServletRequest.class);
    Authentication authentication = mock(Authentication.class);
    SecurityContext securityContext = mock(SecurityContext.class);

    SecurityContextHolder.setContext(securityContext);

    when(authentication.getPrincipal()).thenReturn(user);
    when(userService.loadUserById(id)).thenReturn(user);

    String jwt = securityService.generateToken(authentication);

    securityService.setAuthenticationFromJwt(jwt, request);

    ArgumentCaptor captor = ArgumentCaptor.forClass(Authentication.class);
    verify(securityContext, times(1)).setAuthentication((Authentication) captor.capture());
    UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) captor.getValue();

    assertEquals(user, authenticationToken.getPrincipal());
    assertEquals(permissions, authenticationToken.getAuthorities());
  }

  @Test
  public void getUserIdFromJwt() {
    Long id = 1L;

    User user = new User();
    user.setId(id);

    Authentication authentication = mock(Authentication.class);
    when(authentication.getPrincipal()).thenReturn(user);

    String jwt = securityService.generateToken(authentication);

    assertEquals(id, securityService.getUserIdFromJwt(jwt));
  }
}