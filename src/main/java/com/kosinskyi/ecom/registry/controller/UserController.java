package com.kosinskyi.ecom.registry.controller;

import com.kosinskyi.ecom.registry.dto.response.user.UserResponse;
import com.kosinskyi.ecom.registry.mapping.user.UserMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/users")
public class UserController {

  private UserMapper mapper;

  public UserController(UserMapper mapper) {
    this.mapper = mapper;
  }

  @GetMapping("current")
  public ResponseEntity<UserResponse> getCurrentUser() {
    return ResponseEntity.ok(mapper.getCurrentUser());
  }

}
