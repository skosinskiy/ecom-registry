package com.kosinskyi.ecom.registry.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kosinskyi.ecom.registry.dto.response.error.UnauthorizedErrorResponse;
import com.kosinskyi.ecom.registry.entity.User;
import com.kosinskyi.ecom.registry.security.SecurityService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
public class SecurityController {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void accessRestrictedResourceTest() throws Exception {
		String expectedMessage = "Full authentication is required to access this resource";

		MvcResult result = mockMvc.perform(get("/")).andReturn();

		MockHttpServletResponse response = result.getResponse();
		UnauthorizedErrorResponse errorResponse =
				objectMapper.readValue(response.getContentAsString(), UnauthorizedErrorResponse.class);

		assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
		assertEquals(HttpStatus.UNAUTHORIZED.name(), response.getErrorMessage());
		assertEquals(HttpStatus.UNAUTHORIZED.value(), errorResponse.getStatus());
		assertEquals(HttpStatus.UNAUTHORIZED.name(), errorResponse.getError());
		assertEquals(expectedMessage, errorResponse.getMessage());
		assertNotNull(errorResponse.getPath());
		assertNotNull(errorResponse.getTimeStamp());
	}

	@Test
	public void accessAllowedTest() throws Exception {
		Authentication authentication = mock(Authentication.class);
		User user = new User();
		user.setId(1L);
		user.setEmail("stanislav.kosinski@gmail.com");
		when(authentication.getPrincipal()).thenReturn(user);

		String jwt = securityService.generateToken(authentication);
		MvcResult result = mockMvc.perform(
				get("/").header("Authorization", "Bearer " + jwt)

		).andReturn();

		int responseStatus = result.getResponse().getStatus();

		assertEquals(HttpServletResponse.SC_NOT_FOUND, responseStatus);
	}

	@Test
	public void accessRestrictedExpiretJwtTest() throws Exception {
		String jwt = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNTYyMDU4OTM3LCJleHAiOjE1NjIwNTg5Mzd9.IqRmKcOKhH-QMHUDC5AixZoP58E6tTKneVrZ1OMJ0n_rqU3f_-2VpyyTn7ZyXrPJptDsLRg-cTG8uKMpLiz3mw";
		MvcResult result = mockMvc.perform(
				get("/").header("Authorization", "Bearer " + jwt)

		).andReturn();

		MockHttpServletResponse response = result.getResponse();
		UnauthorizedErrorResponse errorResponse =
				objectMapper.readValue(response.getContentAsString(), UnauthorizedErrorResponse.class);

		assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
		assertEquals(HttpStatus.UNAUTHORIZED.name(), response.getErrorMessage());
		assertEquals(HttpStatus.UNAUTHORIZED.value(), errorResponse.getStatus());
		assertEquals(HttpStatus.UNAUTHORIZED.name(), errorResponse.getError());
		assertTrue(errorResponse.getMessage().startsWith("JWT expired"));
		assertNotNull(errorResponse.getPath());
		assertNotNull(errorResponse.getTimeStamp());
	}


}
