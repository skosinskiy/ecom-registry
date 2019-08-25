package com.kosinskyi.ecom.registry.dto.response;

import lombok.Data;

@Data
public class LoginResponse {

  private String jwtAccessToken;
  private String jwtRefreshToken;
  private Long jwtRefreshTokenExpireDate;
  private String tokenType;

}