package com.kosinskyi.ecom.registry.service.registry.daily.parsing.cache;

import com.kosinskyi.ecom.registry.entity.registry.daily.DailyRegistryParseCriteria;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
public class DailyRegistryParseCache {

  private Long id;
  private LocalDate date;
  private List<DailyRegistryParseCriteria> parseCriteriaList;
  private Map<DailyRegistryParseCriteria, Integer> criteriaColumnIndexCache;

}
