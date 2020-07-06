package com.kosinskyi.ecom.registry.service.user;

import com.kosinskyi.ecom.registry.entity.user.User;
import com.kosinskyi.ecom.registry.entity.user.User_;
import com.kosinskyi.ecom.registry.entity.user.specification.UserSpecification;
import com.kosinskyi.ecom.registry.error.exception.ActionForbiddenException;
import com.kosinskyi.ecom.registry.repository.base.JpaSpecificationExecutorRepository;
import com.kosinskyi.ecom.registry.repository.user.UserRepository;
import com.kosinskyi.ecom.registry.service.crud.CreateService;
import com.kosinskyi.ecom.registry.service.crud.ReadService;
import com.kosinskyi.ecom.registry.service.crud.UpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService, ReadService<User>, CreateService<User>, UpdateService<User> {

  private final UserRepository repository;
  private final UserSpecification specification;

  @Autowired
  public UserService(UserRepository repository, UserSpecification specification) {
    this.repository = repository;
    this.specification = specification;
  }

  @Override
  public JpaSpecificationExecutorRepository<User, Long> repositorySupplier() {
    return repository;
  }

  @Override
  public Class<User> entityClassSupplier() {
    return User.class;
  }

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String email) {
    return findByEmail(email);
  }

  public User getCurrentUser() {
    String email = SecurityContextHolder.getContext().getAuthentication().getName();
    return findByEmail(email);
  }

  public User setRefreshToken(User user, long jwtRefreshTokenExpirationInMs) {
    if (user.getId() == null) {
      throw new ActionForbiddenException("User id must not be null when set refresh token");
    }
    user.setJwtRefreshToken(UUID.randomUUID().toString());
    user.setJwtRefreshTokenExpireDate(getJwtRefreshTokenExpireTimeInMs(jwtRefreshTokenExpirationInMs));
    return repository.save(user);
  }

  private Date getJwtRefreshTokenExpireTimeInMs(long jwtRefreshTokenExpirationInMs) {
    return new Date(System.currentTimeMillis() + jwtRefreshTokenExpirationInMs);
  }

  public User findByRefreshToken(String jwtRefreshToken) {
    return findOne(specification.entityFieldEquals(User_.jwtRefreshToken, jwtRefreshToken));
  }

  public User findByEmail(String email) {
    return findOne(specification.entityFieldEquals(User_.email, email));
  }
}
