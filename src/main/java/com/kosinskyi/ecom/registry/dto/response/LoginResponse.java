package com.kosinskyi.ecom.registry.dto.response;

import lombok.Data;

@Data
public class LoginResponse {

  private String jwt;
  private String tokenType;

}
