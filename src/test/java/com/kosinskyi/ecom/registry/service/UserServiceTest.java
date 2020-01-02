package com.kosinskyi.ecom.registry.service;

import com.kosinskyi.ecom.registry.entity.User;
import com.kosinskyi.ecom.registry.error.exception.ActionForbiddenException;
import com.kosinskyi.ecom.registry.error.exception.NoDataFoundException;
import com.kosinskyi.ecom.registry.error.exception.NotYetImplementedException;
import com.kosinskyi.ecom.registry.repository.UserRepository;
import com.kosinskyi.ecom.registry.utils.ObjectUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.Principal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceTest {

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Autowired
  private UserService userService;

  @SpyBean
  private ObjectUtils objectUtils;

  @MockBean
  private UserRepository userRepository;

  @Test
  public void loadUserByUsernameTest() {
    String email = "email";
    User user = new User();
    user.setEmail(email);

    when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

    UserDetails result = userService.loadUserByUsername(email);

    assertEquals(user, result);
  }

  @Test
  public void loadUserByUsernameExceptionTest() {
    String email = "email";

    when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

    expectedException.expect(UsernameNotFoundException.class);
    expectedException.expectMessage(String.format("No user with email %s found", email));

    userService.loadUserByUsername(email);
  }

  @Test
  public void getCurrentUserTest() {
    String email = "email";
    Principal principal = mock(Principal.class);
    User user = new User();
    user.setEmail(email);

    when(principal.getName()).thenReturn(email);
    when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

    User result = userService.getCurrentUser(principal);

    assertEquals(user, result);
  }

  @Test
  public void findByIdTest() {
    Long userId = 1L;
    User user = new User();
    user.setId(userId);

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));

    User result = userService.findById(userId);

    assertEquals(user, result);
  }

  @Test
  public void findByIdExceptionTest() {
    Long userId = 1L;

    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    expectedException.expect(NoDataFoundException.class);
    expectedException.expectMessage(String.format("No user with id %d found", userId));

    userService.findById(userId);
  }

  @Test
  public void findAllTest() {
    expectedException.expect(NotYetImplementedException.class);
    userService.findAll();
  }

  @Test
  public void findAllPageableTest() {
    Pageable pageable = mock(Pageable.class);
    expectedException.expect(NotYetImplementedException.class);
    userService.findAll(pageable);
  }

  @Test
  public void createTest() {
    Long userId = 1L;
    String email = "email";
    User user = new User();
    user.setId(userId);
    user.setEmail(email);

    User expectedUser = new User();
    user.setEmail(email);

    when(userRepository.save(any(User.class))).thenReturn(expectedUser);

    User result = userService.create(user);

    ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
    verify(userRepository, times(1)).save(captor.capture());
    User capturedUser = captor.getValue();

    assertEquals(expectedUser, result);
    assertNull(capturedUser.getId());
  }

  @Test
  public void updateTest() {
    Long userId = 1L;
    String existingFirstName = "existingFirstName";
    String updatedEmail = "updatedEmail";

    User existingUser = new User();
    existingUser.setFirstName(existingFirstName);
    existingUser.setId(userId);

    User updatedUser = new User();
    updatedUser.setEmail(updatedEmail);
    updatedUser.setFirstName(null);

    when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
    when(userRepository.save(any(User.class))).thenReturn(updatedUser);

    User result = userService.update(userId, updatedUser);

    ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
    verify(userRepository, times(1)).save(captor.capture());
    User capturedUser = captor.getValue();

    assertEquals(updatedUser, result);
    assertEquals(userId, capturedUser.getId());
    assertEquals(updatedEmail, capturedUser.getEmail());
    assertEquals(existingFirstName, capturedUser.getFirstName());
    verify(objectUtils, times(1)).copyNotNullProperties(updatedUser, existingUser);
  }

  @Test
  public void deleteTest() {
    Long userId = 1L;
    expectedException.expect(NotYetImplementedException.class);
    userService.delete(userId);
  }

  @Test
  public void setRefreshTokenTest() {
    long jwtExpirationTimeInMs = 1000000L;
    Long userId = 1L;
    User user = new User();
    user.setId(userId);
    int expectedTokenLength = 36;

    when(userRepository.save(any(User.class))).thenReturn(user);

    User result = userService.setRefreshToken(user, jwtExpirationTimeInMs);

    ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
    verify(userRepository, times(1)).save(captor.capture());
    User capturedValue = captor.getValue();

    assertEquals(user, result);
    assertNotNull(capturedValue.getJwtRefreshToken());
    assertEquals(expectedTokenLength, capturedValue.getJwtRefreshToken().length());
    assertTrue(System.currentTimeMillis() + jwtExpirationTimeInMs -
        capturedValue.getJwtRefreshTokenExpireDate().getTime() < 1000 );
  }

  @Test
  public void setRefreshTokenExceptionTest() {
    long jwtExpirationTimeInMs = 1000000L;

    expectedException.expect(ActionForbiddenException.class);
    expectedException.expectMessage("User id must not be null when set refresh token");

    userService.setRefreshToken(new User(), jwtExpirationTimeInMs);
  }

  @Test
  public void findUserByRefreshTokenTest() {
    String jwtRefreshToken = "jwtRefreshToken";
    User user = new User();
    user.setJwtRefreshToken(jwtRefreshToken);

    when(userRepository.findByJwtRefreshToken(jwtRefreshToken)).thenReturn(Optional.of(user));

    User result = userService.findUserByRefreshToken(jwtRefreshToken);

    assertEquals(user, result);
  }

  @Test
  public void findUserByRefreshTokenExceptionTest() {
    String jwtRefreshToken = "jwtRefreshToken";

    when(userRepository.findByJwtRefreshToken(jwtRefreshToken)).thenReturn(Optional.empty());

    expectedException.expect(NoDataFoundException.class);
    expectedException.expectMessage(String.format("No user found with refresh token %s", jwtRefreshToken));

    userService.findUserByRefreshToken(jwtRefreshToken);
  }
}
