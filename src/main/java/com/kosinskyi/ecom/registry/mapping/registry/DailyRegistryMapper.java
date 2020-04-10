package com.kosinskyi.ecom.registry.mapping.registry;

import com.kosinskyi.ecom.registry.dto.response.registry.daily.DailyRegistryResponse;
import com.kosinskyi.ecom.registry.entity.registry.daily.DailyRegistry;
import com.kosinskyi.ecom.registry.mapping.base.crud.DeleteMapper;
import com.kosinskyi.ecom.registry.mapping.base.crud.ReadMapper;
import com.kosinskyi.ecom.registry.service.crud.DeleteService;
import com.kosinskyi.ecom.registry.service.crud.ReadService;
import com.kosinskyi.ecom.registry.service.registry.daily.DailyRegistryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Component
public class DailyRegistryMapper implements ReadMapper<DailyRegistry>, DeleteMapper<DailyRegistry> {

  private DailyRegistryService service;
  private ModelMapper modelMapper;

  @Autowired
  public DailyRegistryMapper(DailyRegistryService service, ModelMapper modelMapper) {
    this.service = service;
    this.modelMapper = modelMapper;
  }

  @Override
  public ReadService<DailyRegistry> readServiceSupplier() {
    return service;
  }

  @Override
  public DeleteService<DailyRegistry> deleteServiceSupplier() {
    return service;
  }

  @Override
  public ModelMapper modelMapperSupplier() {
    return modelMapper;
  }

  public DailyRegistryResponse create(LocalDate date, MultipartFile multipartFile) {
    DailyRegistry dailyRegistry = service.create(date, multipartFile);
    return mapEntityToResponseDto(dailyRegistry, DailyRegistryResponse.class);
  }

  public byte[] getBinary(Long id) {
    return service.getBinary(id);
  }

  public DailyRegistryResponse parse(Long registryId) {
    return mapEntityToResponseDto(service.parse(registryId), DailyRegistryResponse.class);
  }
}
