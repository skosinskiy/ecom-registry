package com.kosinskyi.ecom.registry.controller;

import com.kosinskyi.ecom.registry.logging.Logging;
import com.kosinskyi.ecom.registry.mapping.UserMapper;
import com.kosinskyi.ecom.registry.dto.response.UserResponse;
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

  private UserMapper userMapper;

  public UserController(UserMapper userMapper) {
    this.userMapper = userMapper;
  }

  @GetMapping("current")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<UserResponse> getCurrentUser(Principal principal) {
    return ResponseEntity.ok(userMapper.getCurrentUser(principal));
  }

}
