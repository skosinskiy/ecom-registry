package com.kosinskyi.ecom.registry.mapping;

import com.kosinskyi.ecom.registry.dto.response.registry.DailyRegistryResponse;
import com.kosinskyi.ecom.registry.entity.registry.DailyRegistry;
import com.kosinskyi.ecom.registry.service.registry.DailyRegistryService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;
import java.time.LocalDate;

@Component
public class DailyRegistryMapper extends AbstractMapper<DailyRegistry, DailyRegistryService> {

  public DailyRegistryResponse create(LocalDate date, MultipartFile multipartFile) {
    DailyRegistry dailyRegistry = crudService.create(date, multipartFile);
    return mapEntityToResponseDto(dailyRegistry, DailyRegistryResponse.class);
  }

  public byte[] getBinary(Long id) {
    return crudService.getBinary(id);
  }

  public Page<DailyRegistryResponse> findAll(Pageable pageable) {
    Page<DailyRegistry> page = crudService.findAll(pageable);
    return page.map(dailyRegistry -> mapEntityToResponseDto(dailyRegistry, DailyRegistryResponse.class));
  }

  public DailyRegistryResponse delete(Long registryId) {
    DailyRegistry dailyRegistry = crudService.delete(registryId);
    return mapEntityToResponseDto(dailyRegistry, DailyRegistryResponse.class);
  }
}
