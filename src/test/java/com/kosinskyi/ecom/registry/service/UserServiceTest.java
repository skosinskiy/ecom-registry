package com.kosinskyi.ecom.registry.service;

import com.kosinskyi.ecom.registry.entity.User;
import com.kosinskyi.ecom.registry.repository.UserRepository;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceTest {

  @Autowired
  private UserService userService;

  @MockBean
  private UserRepository userRepository;

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Test
  public void loadUserByUsernameTest() {
    Long id = 1L;
    String email = "stanislav.kosinski@gmail.com";

    User user = new User();
    user.setEmail(email);
    user.setId(id);

    when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

    UserDetails userDetails = userService.loadUserByUsername(email);

    assertEquals(userDetails, user);
  }

  @Test
  public void loadUserByUsernameExceptinTest() {
    String email = "stanislav.kosinski@gmail.com";

    when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

    expectedException.expect(UsernameNotFoundException.class);
    expectedException.expectMessage("User not found with email : " + email);

    userService.loadUserByUsername(email);
  }

  @Test
  public void loadUserByIdTest() {
    Long id = 1L;
    String email = "stanislav.kosinski@gmail.com";

    User user = new User();
    user.setEmail(email);
    user.setId(id);

    when(userRepository.findById(id)).thenReturn(Optional.of(user));

    UserDetails userDetails = userService.loadUserById(id);

    assertEquals(userDetails, user);
  }

  @Test
  public void loadUserByIdExceptionTest() {
    Long id = 1L;

    when(userRepository.findById(id)).thenReturn(Optional.empty());

    expectedException.expect(UsernameNotFoundException.class);
    expectedException.expectMessage("User not found with id : " + id);

    userService.loadUserById(id);
  }
}