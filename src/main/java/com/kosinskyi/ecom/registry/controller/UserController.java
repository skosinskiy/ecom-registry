package com.kosinskyi.ecom.registry.controller;

import com.kosinskyi.ecom.registry.convert.UserConvertFacade;
import com.kosinskyi.ecom.registry.dto.response.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("api/users")
public class UserController {

  private UserConvertFacade userConvertFacade;

  public UserController(UserConvertFacade userConvertFacade) {
    this.userConvertFacade = userConvertFacade;
  }

  @GetMapping("current")
  public ResponseEntity<UserResponse> getCurrentUser(Principal principal) {
    return ResponseEntity.ok(userConvertFacade.getCurrentUser(principal));
  }

}
