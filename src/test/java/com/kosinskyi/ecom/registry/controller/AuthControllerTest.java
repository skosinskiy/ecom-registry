package com.kosinskyi.ecom.registry.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kosinskyi.ecom.registry.dto.request.LoginRequest;
import com.kosinskyi.ecom.registry.dto.response.ErrorResponse;
import com.kosinskyi.ecom.registry.dto.response.LoginResponse;
import com.kosinskyi.ecom.registry.entity.Permission;
import com.kosinskyi.ecom.registry.entity.User;
import com.kosinskyi.ecom.registry.security.SecurityService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
public class AuthControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private SecurityService securityService;

	@Test
	public void signinSuccessTest() throws Exception {
		Long expectedUserId = 1L;
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setEmail("stanislav.kosinski@gmail.com");
		loginRequest.setPassword("admin");

		String loginRequestJson = objectMapper.writeValueAsString(loginRequest);

		MvcResult result = mockMvc.perform(
				post("/api/auth/signin")
						.content(loginRequestJson)
						.contentType(MediaType.APPLICATION_JSON))
				.andReturn();

		String responseBody = result.getResponse().getContentAsString();
		LoginResponse loginResponse = objectMapper.readValue(responseBody, LoginResponse.class);

		assertEquals(SecurityService.TOKEN_TYPE, loginResponse.getTokenType());
		assertTrue(securityService.isTokenValid(loginResponse.getJwt()));
		assertEquals(expectedUserId, securityService.getUserIdFromJwt(loginResponse.getJwt()));
	}

	@Test
	public void signinExpiredUserTest() throws Exception {
		String expectedErrorMessage = "User account has expired";
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setEmail("expiredUser@gmail.com");
		loginRequest.setPassword("admin");

		String loginRequestJson = objectMapper.writeValueAsString(loginRequest);

		MvcResult result = mockMvc.perform(
				post("/api/auth/signin")
						.content(loginRequestJson)
						.contentType(MediaType.APPLICATION_JSON))
				.andReturn();

		MockHttpServletResponse response = result.getResponse();
    ErrorResponse errorResponse =
				objectMapper.readValue(response.getContentAsString(), ErrorResponse.class);

		assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
		assertEquals(HttpStatus.UNAUTHORIZED.name(), response.getErrorMessage());
		assertEquals(HttpStatus.UNAUTHORIZED.value(), errorResponse.getStatus());
		assertEquals(HttpStatus.UNAUTHORIZED.name(), errorResponse.getError());
		assertEquals(expectedErrorMessage, errorResponse.getMessage());
		assertNotNull(errorResponse.getPath());
		assertNotNull(errorResponse.getTimeStamp());
	}

	@Test
	public void signinWrongPasswordTest() throws Exception {
		String expectedErrorMessage = "Bad credentials";
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setEmail("stanislav.kosinski@gmail.com");
		loginRequest.setPassword("wrongPassword");

		String loginRequestJson = objectMapper.writeValueAsString(loginRequest);

		MvcResult result = mockMvc.perform(
				post("/api/auth/signin")
						.content(loginRequestJson)
						.contentType(MediaType.APPLICATION_JSON))
				.andReturn();

		MockHttpServletResponse response = result.getResponse();
    ErrorResponse errorResponse =
				objectMapper.readValue(response.getContentAsString(), ErrorResponse.class);

		assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
		assertEquals(HttpStatus.UNAUTHORIZED.name(), response.getErrorMessage());
		assertEquals(HttpStatus.UNAUTHORIZED.value(), errorResponse.getStatus());
		assertEquals(HttpStatus.UNAUTHORIZED.name(), errorResponse.getError());
		assertEquals(expectedErrorMessage, errorResponse.getMessage());
		assertNotNull(errorResponse.getPath());
		assertNotNull(errorResponse.getTimeStamp());
	}

	@Test
	public void signinWrongUserTest() throws Exception {
		String expectedErrorMessage = "Bad credentials";
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setEmail("wrongUser@gmail.com");
		loginRequest.setPassword("wrongPassword");

		String loginRequestJson = objectMapper.writeValueAsString(loginRequest);

		MvcResult result = mockMvc.perform(
				post("/api/auth/signin")
						.content(loginRequestJson)
						.contentType(MediaType.APPLICATION_JSON))
				.andReturn();

		MockHttpServletResponse response = result.getResponse();
    ErrorResponse errorResponse =
				objectMapper.readValue(response.getContentAsString(), ErrorResponse.class);

		assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
		assertEquals(HttpStatus.UNAUTHORIZED.name(), response.getErrorMessage());
		assertEquals(HttpStatus.UNAUTHORIZED.value(), errorResponse.getStatus());
		assertEquals(HttpStatus.UNAUTHORIZED.name(), errorResponse.getError());
		assertEquals(expectedErrorMessage, errorResponse.getMessage());
		assertNotNull(errorResponse.getPath());
		assertNotNull(errorResponse.getTimeStamp());
	}

	@Test
	public void testHasAuthority() throws Exception {
    Authentication authentication = mock(Authentication.class);
    List<Permission> permissions = new ArrayList<>();
    permissions.add(Permission.MANAGE_REGISTRY);
    User user = new User();
    user.setId(1L);
    user.setEmail("stanislav.kosinski@gmail.com");
    user.setPermissions(permissions);
    when(authentication.getPrincipal()).thenReturn(user);

    String jwt = securityService.generateToken(authentication);
    MvcResult result = mockMvc.perform(
        get("/api/auth/test").header("Authorization", "Bearer " + jwt)

    ).andReturn();

    assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    assertEquals("OK!", result.getResponse().getContentAsString());
	}

  @Test
  public void testHasNotAuthority() throws Exception {
    Authentication authentication = mock(Authentication.class);
    List<Permission> permissions = new ArrayList<>();
    permissions.add(Permission.MANAGE_REGISTRY);
    User user = new User();
    user.setId(1L);
    user.setEmail("stanislav.kosinski@gmail.com");
    user.setPermissions(permissions);
    when(authentication.getPrincipal()).thenReturn(user);

    String jwt = securityService.generateToken(authentication);
    MvcResult result = mockMvc.perform(
        get("/api/auth/test2").header("Authorization", "Bearer " + jwt)

    ).andReturn();

    ErrorResponse errorResponse =
        objectMapper.readValue(result.getResponse().getContentAsString(), ErrorResponse.class);

    assertEquals(HttpStatus.FORBIDDEN.value(), result.getResponse().getStatus());
    assertEquals(HttpStatus.FORBIDDEN.name(), result.getResponse().getErrorMessage());
    assertEquals(HttpStatus.FORBIDDEN.name(), errorResponse.getError());
    assertEquals(HttpStatus.FORBIDDEN.value(), errorResponse.getStatus());
    assertEquals("Access is denied", errorResponse.getMessage());
  }
}