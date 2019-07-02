package com.kosinskyi.ecom.registry.dto.response.error;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class InternalErrorResponse extends ErrorResponse {

  private int status;
  private String error;

  public InternalErrorResponse(Exception exc, HttpServletRequest request) {
    super(exc, request);
    this.status = HttpStatus.INTERNAL_SERVER_ERROR.value();
    this.error = HttpStatus.INTERNAL_SERVER_ERROR.name();
  }

}
