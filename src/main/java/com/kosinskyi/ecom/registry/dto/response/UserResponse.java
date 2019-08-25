package com.kosinskyi.ecom.registry.dto.response;

import com.kosinskyi.ecom.registry.entity.Permission;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UserResponse {

  private String email;
  private String firstName;
  private String lastName;
  private Date accountExpireDate;
  private List<Permission> permissions;

}
