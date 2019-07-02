package com.kosinskyi.ecom.registry.dto.response.error;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class UnauthorizedErrorResponse extends ErrorResponse {

  private int status;
  private String error;

  public UnauthorizedErrorResponse(Exception exc, HttpServletRequest request) {
    super(exc, request);
    this.status = HttpStatus.UNAUTHORIZED.value();
    this.error = HttpStatus.UNAUTHORIZED.name();
  }
}
