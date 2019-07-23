package com.kosinskyi.ecom.registry.convert;

import com.kosinskyi.ecom.registry.dto.request.UserRequest;
import com.kosinskyi.ecom.registry.dto.response.UserResponse;
import com.kosinskyi.ecom.registry.entity.User;
import com.kosinskyi.ecom.registry.service.UserService;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
public class UserConvertFacade extends AbstractConvertFacade<User, UserRequest, UserResponse> {

  public UserResponse getCurrentUser(Principal principal) {
    UserService userService = (UserService) crudService;
    return mapEntityToResponseDto(userService.getCurrentUser(principal));
  }
}
