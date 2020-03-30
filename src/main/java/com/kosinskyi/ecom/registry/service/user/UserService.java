package com.kosinskyi.ecom.registry.service.user;

import com.kosinskyi.ecom.registry.entity.user.User;
import com.kosinskyi.ecom.registry.exception.ActionForbiddenException;
import com.kosinskyi.ecom.registry.exception.NoDataFoundException;
import com.kosinskyi.ecom.registry.exception.NotYetImplementedException;
import com.kosinskyi.ecom.registry.repository.UserRepository;
import com.kosinskyi.ecom.registry.service.CrudService;
import com.kosinskyi.ecom.registry.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService, CrudService<User> {

  private UserRepository userRepository;
  private ObjectUtils objectUtils;

  @Autowired
  public UserService(UserRepository userRepository, ObjectUtils objectUtils) {
    this.userRepository = userRepository;
    this.objectUtils = objectUtils;
  }

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String email) {
    return userRepository
        .findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException(String.format("No user with email %s found", email)));
  }

  public User getCurrentUser() {
    String principalName = SecurityContextHolder.getContext().getAuthentication().getName();
    return userRepository
        .findByEmail(principalName)
        .orElseThrow(() -> new NoDataFoundException(String.format("No user with id %s found", principalName)));
  }

  @Override
  public User findById(Long userId) {
    return userRepository
        .findById(userId)
        .orElseThrow(() -> new NoDataFoundException(String.format("No user with id %d found", userId)));
  }

  @Override
  public List<User> findAll() {
    throw new NotYetImplementedException();
  }

  @Override
  public Page<User> findAll(Pageable pageable) {
    throw new NotYetImplementedException();
  }

  @Override
  public User create(User user) {
    user.setId(null);
    return userRepository.save(user);
  }

  @Override
  public User update(Long userId, User updatedEntity) {
    User existingUser = findById(userId);
    objectUtils.copyNotNullProperties(updatedEntity, existingUser);
    return userRepository.save(existingUser);
  }

  @Override
  public User delete(Long userId) {
    throw new NotYetImplementedException();
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
