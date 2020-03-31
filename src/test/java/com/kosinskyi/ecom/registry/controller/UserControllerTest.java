package com.kosinskyi.ecom.registry.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kosinskyi.ecom.registry.dto.response.user.UserResponse;
import com.kosinskyi.ecom.registry.entity.user.User;
import com.kosinskyi.ecom.registry.repository.UserRepository;
import com.kosinskyi.ecom.registry.service.user.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
@AutoConfigureMockMvc
@WithMockUser(username = "stanislav.kosinski@gmail.com")
public class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private UserService userService;

  @Autowired
  private UserRepository userRepository;

  @Test
  public void getCurrentUserTest() throws Exception {
    String expectedEmail = "stanislav.kosinski@gmail.com";
    User expectedUser = userRepository.findByEmail(expectedEmail).orElseThrow(NoSuchElementException::new);

    MockHttpServletResponse response = mockMvc.perform(get("/api/users/current")).andReturn().getResponse();

    UserResponse userResponse = objectMapper.readValue(response.getContentAsString(), UserResponse.class);

    assertEquals(HttpStatus.OK.value(), response.getStatus());
    assertEquals(expectedUser.getEmail(), userResponse.getEmail());
    assertEquals(expectedUser.getFirstName(), userResponse.getFirstName());
    assertEquals(expectedUser.getLastName(), userResponse.getLastName());
    assertEquals(expectedUser.getAuthorities().size(), userResponse.getPermissions().size());
    assertEquals(expectedUser.getAccountExpireDate().getTime(), userResponse.getAccountExpireDate().getTime());
  }

}
