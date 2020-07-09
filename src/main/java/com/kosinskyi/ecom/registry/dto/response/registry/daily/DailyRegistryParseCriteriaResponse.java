package com.kosinskyi.ecom.registry.dto.response.registry.daily;

import lombok.Data;

import java.util.List;

@Data
public class DailyRegistryParseCriteriaResponse {

  private String name;
  private String filterColumnName;
  private List<String> filterValues;

}
