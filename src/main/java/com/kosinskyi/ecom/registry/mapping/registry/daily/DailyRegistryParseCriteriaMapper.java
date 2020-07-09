package com.kosinskyi.ecom.registry.mapping.registry.daily;

import com.kosinskyi.ecom.registry.entity.registry.daily.DailyRegistryParseCriteria;
import com.kosinskyi.ecom.registry.mapping.base.crud.ReadMapper;
import com.kosinskyi.ecom.registry.service.crud.ReadService;
import com.kosinskyi.ecom.registry.service.registry.daily.parsing.DailyRegistryParseCriteriaService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DailyRegistryParseCriteriaMapper implements ReadMapper<DailyRegistryParseCriteria> {

  private final DailyRegistryParseCriteriaService service;
  private final ModelMapper modelMapper;

  @Autowired
  public DailyRegistryParseCriteriaMapper(DailyRegistryParseCriteriaService service, ModelMapper modelMapper) {
    this.service = service;
    this.modelMapper = modelMapper;
  }

  @Override
  public ReadService<DailyRegistryParseCriteria> readServiceSupplier() {
    return service;
  }

  @Override
  public ModelMapper modelMapperSupplier() {
    return modelMapper;
  }
}
