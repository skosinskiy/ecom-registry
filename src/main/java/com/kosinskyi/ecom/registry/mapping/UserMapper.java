package com.kosinskyi.ecom.registry.mapping;

import com.kosinskyi.ecom.registry.dto.request.UserRequest;
import com.kosinskyi.ecom.registry.dto.response.UserResponse;
import com.kosinskyi.ecom.registry.entity.User;
import com.kosinskyi.ecom.registry.service.UserService;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
public class UserMapper extends AbstractMapper<User, UserRequest, UserResponse> {

  private UserService userService = (UserService) crudService;

  public UserResponse getCurrentUser(Principal principal) {
    return mapEntityToResponseDto(userService.getCurrentUser(principal));
  }
}
