package com.kosinskyi.ecom.registry.dto.response.error;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Data
@NoArgsConstructor
public class ErrorResponse {

  private Date timeStamp;
  private int status;
  private String error;
  private String message;
  private String path;

  public ErrorResponse(Exception exc, HttpServletRequest request, HttpStatus httpStatus) {
    this.timeStamp = new Date();
    this.status = httpStatus.value();
    this.error = httpStatus.name();
    this.message = exc.getMessage();
    this.path = request.getServletPath();
  }
}
