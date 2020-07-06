package com.kosinskyi.ecom.registry.mapping.user;

import com.kosinskyi.ecom.registry.dto.response.user.UserResponse;
import com.kosinskyi.ecom.registry.mapping.base.BaseMapper;
import com.kosinskyi.ecom.registry.service.user.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements BaseMapper {

  private final ModelMapper modelMapper;
  private final UserService userService;

  @Autowired
  public UserMapper(ModelMapper modelMapper, UserService userService) {
    this.modelMapper = modelMapper;
    this.userService = userService;
  }

  @Override
  public ModelMapper modelMapperSupplier() {
    return modelMapper;
  }

  public UserResponse getCurrentUser() {
    return mapEntityToResponseDto(userService.getCurrentUser(), UserResponse.class);
  }


}
