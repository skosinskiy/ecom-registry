package com.kosinskyi.ecom.registry.dto.request;

import lombok.Data;

@Data
public class RefreshRequest {

  private String jwtRefreshToken;

}
