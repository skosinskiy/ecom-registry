package com.kosinskyi.ecom.registry.dto.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class FileItemRequest {

  @NotNull
  private Date fileItemDate;

}
