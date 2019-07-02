package com.kosinskyi.ecom.registry.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kosinskyi.ecom.registry.dto.request.LoginRequest;
import com.kosinskyi.ecom.registry.dto.response.LoginResponse;
import com.kosinskyi.ecom.registry.dto.response.error.UnauthorizedErrorResponse;
import com.kosinskyi.ecom.registry.security.SecurityService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
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
		UnauthorizedErrorResponse errorResponse =
				objectMapper.readValue(response.getContentAsString(), UnauthorizedErrorResponse.class);

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
		UnauthorizedErrorResponse errorResponse =
				objectMapper.readValue(response.getContentAsString(), UnauthorizedErrorResponse.class);

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
		UnauthorizedErrorResponse errorResponse =
				objectMapper.readValue(response.getContentAsString(), UnauthorizedErrorResponse.class);

		assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
		assertEquals(HttpStatus.UNAUTHORIZED.name(), response.getErrorMessage());
		assertEquals(HttpStatus.UNAUTHORIZED.value(), errorResponse.getStatus());
		assertEquals(HttpStatus.UNAUTHORIZED.name(), errorResponse.getError());
		assertEquals(expectedErrorMessage, errorResponse.getMessage());
		assertNotNull(errorResponse.getPath());
		assertNotNull(errorResponse.getTimeStamp());
	}
}