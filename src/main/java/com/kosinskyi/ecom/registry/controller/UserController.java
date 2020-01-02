package com.kosinskyi.ecom.registry.controller;

import com.kosinskyi.ecom.registry.logging.Logging;
import com.kosinskyi.ecom.registry.mapping.UserCrudMapper;
import com.kosinskyi.ecom.registry.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("api/users")
@Logging
public class UserController {

  private UserCrudMapper userMapper;

  public UserController(UserCrudMapper userMapper) {
    this.userMapper = userMapper;
  }

  @GetMapping
  @PreAuthorize("hasAuthority('MANAGE_ACCOUNTS')")
  public ResponseEntity<Page<UserResponse>> findAll(Pageable pageable) {
    return ResponseEntity.ok(userMapper.findAll(pageable));
  }

  @GetMapping("current")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<UserResponse> getCurrentUser() {
    return ResponseEntity.ok(userMapper.getCurrentUser());
  }

}
