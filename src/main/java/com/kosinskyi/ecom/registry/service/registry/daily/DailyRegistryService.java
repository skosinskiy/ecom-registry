package com.kosinskyi.ecom.registry.service.registry.daily;

import com.kosinskyi.ecom.registry.entity.file.FileItem;
import com.kosinskyi.ecom.registry.entity.registry.daily.DailyRegistry;
import com.kosinskyi.ecom.registry.entity.registry.daily.DailyRegistry_;
import com.kosinskyi.ecom.registry.entity.registry.daily.constants.DailyRegistryStatus;
import com.kosinskyi.ecom.registry.entity.registry.daily.specification.DailyRegistrySpecification;
import com.kosinskyi.ecom.registry.error.exception.ApplicationException;
import com.kosinskyi.ecom.registry.repository.base.JpaSpecificationExecutorRepository;
import com.kosinskyi.ecom.registry.repository.registry.daily.DailyRegistryRepository;
import com.kosinskyi.ecom.registry.service.crud.DeleteService;
import com.kosinskyi.ecom.registry.service.crud.ReadService;
import com.kosinskyi.ecom.registry.service.file.FileItemService;
import com.kosinskyi.ecom.registry.service.registry.daily.parsing.DailyRegistryParseService;
import com.kosinskyi.ecom.registry.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class DailyRegistryService implements ReadService<DailyRegistry>, DeleteService<DailyRegistry> {

  private DailyRegistryRepository jpaRepository;
  private DailyRegistrySpecification specification;
  private FileItemService fileItemService;
  private DailyRegistryParseService parseService;
  private UserService userService;

  @Autowired
  public DailyRegistryService(
      DailyRegistryRepository jpaRepository,
      DailyRegistrySpecification specification,
      FileItemService fileItemService,
      DailyRegistryParseService parseService,
      UserService userService
  ) {
    this.jpaRepository = jpaRepository;
    this.specification = specification;
    this.fileItemService = fileItemService;
    this.parseService = parseService;
    this.userService = userService;
  }

  @Override
  public JpaSpecificationExecutorRepository<DailyRegistry, Long> repositorySupplier() {
    return jpaRepository;
  }

  @Override
  public Class<DailyRegistry> entityClassSupplier() {
    return DailyRegistry.class;
  }

  private Optional<DailyRegistry> findByRegistryDate(LocalDate registryDate) {
    return findOptionalOne(specification.entityFieldEquals(DailyRegistry_.registryDate, registryDate));
  }

  public DailyRegistry create(LocalDate date, MultipartFile multipartFile) {
    findByRegistryDate(date).ifPresent(dailyRegistry -> {
      throw new ApplicationException("Registry with such date already exists");
    });
    DailyRegistry dailyRegistry = new DailyRegistry();
    dailyRegistry.setStatus(DailyRegistryStatus.CREATED);
    dailyRegistry.setRegistryDate(date);
    dailyRegistry.setRegistryItem(fileItemService.uploadFile(multipartFile));
    dailyRegistry.setUser(userService.getCurrentUser());
    dailyRegistry.setId(null);
    return jpaRepository.save(dailyRegistry);
  }

  @Override
  public DailyRegistry delete(Long id) {
    DailyRegistry dailyRegistry = findById(id);
    jpaRepository.delete(dailyRegistry);
    return dailyRegistry;
  }

  public DailyRegistry parse(Long id) {
    DailyRegistry dailyRegistry = findById(id);
    dailyRegistry.setStatus(DailyRegistryStatus.PARSING);
    parseService.parse(dailyRegistry).whenCompleteAsync((file, throwable) -> setParsedRegistryItem(dailyRegistry, file));
    return jpaRepository.save(dailyRegistry);
  }

  private void setParsedRegistryItem(DailyRegistry dailyRegistry, FileItem fileItem) {
    dailyRegistry.setParsedRegistryItem(fileItem);
    dailyRegistry.setStatus(DailyRegistryStatus.PARSED);
    jpaRepository.save(dailyRegistry);
  }
}