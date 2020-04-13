package com.kosinskyi.ecom.registry.dto.response.registry.daily;

import com.kosinskyi.ecom.registry.dto.response.BaseEntityResponse;
import com.kosinskyi.ecom.registry.dto.response.file.FileItemResponse;
import com.kosinskyi.ecom.registry.dto.response.user.UserNameResponse;
import com.kosinskyi.ecom.registry.entity.registry.daily.constants.DailyRegistryStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DailyRegistryResponse extends BaseEntityResponse {

  private LocalDate registryDate;
  private DailyRegistryStatus status;
  private FileItemResponse registryItem;
  private FileItemResponse parsedRegistryItem;
  private UserNameResponse user;

}
