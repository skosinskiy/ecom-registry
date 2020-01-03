package com.kosinskyi.ecom.registry.mapping;

import com.kosinskyi.ecom.registry.dto.request.UserRequest;
import com.kosinskyi.ecom.registry.dto.response.UserResponse;
import com.kosinskyi.ecom.registry.entity.User;
import com.kosinskyi.ecom.registry.service.UserService;
import org.springframework.stereotype.Component;

@Component
public class UserCrudMapper extends AbstractCrudMapper<User, UserService, UserRequest, UserResponse> {

  public UserCrudMapper(UserService userService) {
    super(userService);
  }

  public UserResponse getCurrentUser() {
    return mapEntityToResponseDto(crudService.getCurrentUser());
  }
}
