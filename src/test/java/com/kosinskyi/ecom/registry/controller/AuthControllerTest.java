package com.kosinskyi.ecom.registry.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kosinskyi.ecom.registry.dto.request.LoginRequest;
import com.kosinskyi.ecom.registry.dto.response.LoginResponse;
import com.kosinskyi.ecom.registry.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
@AutoConfigureMockMvc
public class AuthControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private UserService userService;

  @Value("${app.jwtRefreshTokenExpirationInMs}")
  private Long jwtRefreshTokenExpirationInMs;

  @Test
  public void generateJwt() throws Exception {
    String expectedEmail = "stanislav.kosinski@gmail.com";
    String expectedTokenType = "Bearer ";
    LoginRequest loginRequest = new LoginRequest();
    loginRequest.setEmail(expectedEmail);
    loginRequest.setPassword("admin");
    long beforeRequestTime = System.currentTimeMillis();
    MockHttpServletResponse response = mockMvc.perform(post("/api/auth")
        .content(objectMapper.writeValueAsBytes(loginRequest))
        .contentType(MediaType.APPLICATION_JSON)
    ).andReturn().getResponse();

    LoginResponse loginResponse = objectMapper.readValue(response.getContentAsString(), LoginResponse.class);

    assertEquals(HttpStatus.OK.value(), response.getStatus());
    assertNotNull(loginResponse.getJwtAccessToken());
    assertEquals(expectedTokenType, loginResponse.getTokenType());
    assertNotNull(loginResponse.getJwtRefreshToken());
    assertEquals(expectedEmail, userService.findUserByRefreshToken(loginResponse.getJwtRefreshToken()).getEmail());
    assertTrue(loginResponse.getJwtRefreshTokenExpireDate() - beforeRequestTime > jwtRefreshTokenExpirationInMs);
  }

  @Test
  public void refreshJwtAccessToken() {
  }
}