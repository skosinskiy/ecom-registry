package com.kosinskyi.ecom.registry.mapping;

import com.kosinskyi.ecom.registry.dto.response.user.UserResponse;
import com.kosinskyi.ecom.registry.entity.user.User;
import com.kosinskyi.ecom.registry.service.user.UserService;
import org.springframework.stereotype.Component;

@Component
public class UserMapper extends AbstractMapper<User, UserService> {

  public UserResponse getCurrentUser() {
    return mapEntityToResponseDto(crudService.getCurrentUser(), UserResponse.class);
  }
}
