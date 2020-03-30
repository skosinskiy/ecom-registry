package com.kosinskyi.ecom.registry.controller;

import com.kosinskyi.ecom.registry.mapping.UserMapper;
import com.kosinskyi.ecom.registry.dto.response.user.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/users")
public class UserController {

  private UserMapper userConvertFacade;

  public UserController(UserMapper userConvertFacade) {
    this.userConvertFacade = userConvertFacade;
  }

  @GetMapping("current")
  public ResponseEntity<UserResponse> getCurrentUser() {
    return ResponseEntity.ok(userConvertFacade.getCurrentUser());
  }

}
