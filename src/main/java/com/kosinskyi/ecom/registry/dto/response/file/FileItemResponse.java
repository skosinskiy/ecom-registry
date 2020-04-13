package com.kosinskyi.ecom.registry.dto.response.file;

import com.kosinskyi.ecom.registry.dto.response.BaseEntityResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FileItemResponse extends BaseEntityResponse {

  private String key;
  private String extension;
  private Long size;

}
