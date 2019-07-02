package com.kosinskyi.ecom.registry.dto.response.error;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Data
@NoArgsConstructor
public class ErrorResponse {

  private Date timeStamp;
  private String message;
  private String path;

  public ErrorResponse(Exception exc, HttpServletRequest request) {
    this.timeStamp = new Date();
    this.message = exc.getMessage();
    this.path = request.getServletPath();
  }
}
