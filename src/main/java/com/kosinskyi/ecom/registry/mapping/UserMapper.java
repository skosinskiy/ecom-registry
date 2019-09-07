package com.kosinskyi.ecom.registry.mapping;

import com.kosinskyi.ecom.registry.dto.request.UserRequest;
import com.kosinskyi.ecom.registry.dto.response.UserResponse;
import com.kosinskyi.ecom.registry.entity.User;
import com.kosinskyi.ecom.registry.service.UserService;
import org.springframework.stereotype.Component;

@Component
public class UserMapper extends AbstractMapper<User, UserRequest, UserResponse> {

  public UserResponse getCurrentUser() {
    UserService userService = (UserService) crudService;
    return mapEntityToResponseDto(userService.getCurrentUser());
  }
}
