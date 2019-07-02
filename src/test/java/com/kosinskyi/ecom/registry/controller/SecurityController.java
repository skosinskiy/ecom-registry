package com.kosinskyi.ecom.registry.controller;

import com.kosinskyi.ecom.registry.entity.User;
import com.kosinskyi.ecom.registry.security.SecurityService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;

import static org.junit.Assert.assertEquals;
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

	@Test
	public void accessRestrictedResourceTest() throws Exception {
		String expectedErrorMessage = "Full authentication is required to access this resource";

		MvcResult result = mockMvc.perform(get("/")).andReturn();

		String responseErrorMessage = result.getResponse().getErrorMessage();
		int responseErrorCode = result.getResponse().getStatus();

		assertEquals(HttpServletResponse.SC_UNAUTHORIZED, responseErrorCode);
		assertEquals(expectedErrorMessage, responseErrorMessage);
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

}
