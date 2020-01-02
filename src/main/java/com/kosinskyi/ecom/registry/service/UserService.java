package com.kosinskyi.ecom.registry.service;

import com.kosinskyi.ecom.registry.entity.User;
import com.kosinskyi.ecom.registry.error.exception.ActionForbiddenException;
import com.kosinskyi.ecom.registry.logging.Logging;
import com.kosinskyi.ecom.registry.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.Date;
import java.util.UUID;

@Service
@Logging
public class UserService extends AbstractCrudService<User, UserRepository> implements UserDetailsService {

  @Autowired
  public UserService(UserRepository userRepository) {
    super(userRepository);
  }

  public User findByEmail(String email) {
    return findByParam(email, jpaRepository::findByEmail);
  }

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String email) {
    return findByEmail(email);
  }

  public User getCurrentUser() {
    return findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
  }

  public User setRefreshToken(User user, long jwtRefreshTokenExpirationInMs) {
    if (user.getId() == null) {
      throw new ActionForbiddenException("User id must not be null when set refresh token");
    }
    user.setJwtRefreshToken(generateRefreshToken());
    user.setJwtRefreshTokenExpireDate(getJwtRefreshTokenExpireTimeInMs(jwtRefreshTokenExpirationInMs));
    return jpaRepository.save(user);
  }

  private String generateRefreshToken() {
    return UUID.randomUUID().toString();
  }

  private Date getJwtRefreshTokenExpireTimeInMs(long jwtRefreshTokenExpirationInMs) {
    return new Date(System.currentTimeMillis() + jwtRefreshTokenExpirationInMs);
  }

  public User findUserByRefreshToken(String jwtRefreshToken) {
    return findByParam(jwtRefreshToken, jpaRepository::findByJwtRefreshToken);
  }
}
