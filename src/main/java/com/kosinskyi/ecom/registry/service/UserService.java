package com.kosinskyi.ecom.registry.service;

import com.kosinskyi.ecom.registry.entity.User;
import com.kosinskyi.ecom.registry.exception.ActionForbiddenException;
import com.kosinskyi.ecom.registry.exception.NoDataFoundException;
import com.kosinskyi.ecom.registry.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService, CrudService<User> {

  private UserRepository userRepository;

  @Autowired
  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String email) {
    return userRepository
        .findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException(String.format("No user with email %s found", email)));
  }

  @Transactional
  public UserDetails loadUserById(Long id) {
    return userRepository
        .findById(id)
        .orElseThrow(() -> new UsernameNotFoundException(String.format("No user with id %d found", id)));
  }

  public User getCurrentUser(Principal principal) {
    return userRepository
        .findByEmail(principal.getName())
        .orElseThrow(() -> new NoDataFoundException(String.format("No user with id %s found", principal.getName())));
  }

  @Override
  public User getById(Long id) {
    return userRepository
        .findById(id)
        .orElseThrow(() -> new NoDataFoundException(String.format("No user with id %d found", id)));
  }

  @Override
  public List<User> getAll() {
    return userRepository.findAll();
  }

  @Override
  public User create(User user) {
    if (user.getId() != null) {
      throw new ActionForbiddenException("User id is forbidden in create request");
    }
    return userRepository.save(user);
  }

  @Override
  public User update(Long id, User entity) {
    entity.setId(id);
    return userRepository.save(entity);
  }

  @Override
  public User delete(Long id) {
    User user = getById(id);
    userRepository.delete(user);
    return user;
  }

  public User setRefreshToken(User user, long jwtRefreshTokenExpirationInMs) {
    if (user.getId() == null) {
      throw new ActionForbiddenException("User id must not be null when set refresh token");
    }
    user.setJwtRefreshToken(generateRefreshToken());
    user.setJwtRefreshTokenExpireDate(getJwtRefreshTokenExpireTimeInMs(jwtRefreshTokenExpirationInMs));
    return userRepository.save(user);
  }

  private String generateRefreshToken() {
    return UUID.randomUUID().toString();
  }

  private Date getJwtRefreshTokenExpireTimeInMs(long jwtRefreshTokenExpirationInMs) {
    return new Date(System.currentTimeMillis() + jwtRefreshTokenExpirationInMs);
  }

  public User findUserByRefreshToken(String jwtRefreshToken) {
    return userRepository
        .findByJwtRefreshToken(jwtRefreshToken)
        .orElseThrow(() ->
            new NoDataFoundException(String.format("No user found with refresh token %s", jwtRefreshToken)));
  }
}
