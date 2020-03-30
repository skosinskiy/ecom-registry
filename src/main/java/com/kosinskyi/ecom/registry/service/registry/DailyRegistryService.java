package com.kosinskyi.ecom.registry.service.registry;

import com.kosinskyi.ecom.registry.entity.file.FileItem;
import com.kosinskyi.ecom.registry.entity.registry.DailyRegistry;
import com.kosinskyi.ecom.registry.exception.NotYetImplementedException;
import com.kosinskyi.ecom.registry.repository.DailyRegistryRepository;
import com.kosinskyi.ecom.registry.service.BaseCrudService;
import com.kosinskyi.ecom.registry.service.CrudService;
import com.kosinskyi.ecom.registry.service.file.RegistryFileService;
import com.kosinskyi.ecom.registry.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Service
public class DailyRegistryService implements CrudService<DailyRegistry> {

  private RegistryFileService registryFileService;
  private UserService userService;
  private BaseCrudService<DailyRegistry> baseCrudService;

  @Autowired
  public DailyRegistryService(
      DailyRegistryRepository jpaRepository,
      @Qualifier("localRegistryFileService")
      RegistryFileService registryFileService,
      UserService userService
  ) {
    this.registryFileService = registryFileService;
    this.userService = userService;
    this.baseCrudService = new BaseCrudService<>(jpaRepository);
  }

  @Transactional
  public DailyRegistry create(LocalDate date, MultipartFile multipartFile) {
    DailyRegistry dailyRegistry = new DailyRegistry();
    dailyRegistry.setRegistryDate(date);
    dailyRegistry.setFileItem(createFileItem(multipartFile));
    dailyRegistry.setUser(userService.getCurrentUser());
    return baseCrudService.create(dailyRegistry);
  }

  private FileItem createFileItem(MultipartFile multipartFile) {
    String fileKey = registryFileService.uploadFile(multipartFile);
    FileItem fileItem = new FileItem();
    fileItem.setId(null);
    fileItem.setFileKey(fileKey);
    fileItem.setSize(multipartFile.getSize());
    return fileItem;
  }

  @Override
  public DailyRegistry findById(Long id) {
    return baseCrudService.findById(id);
  }

  @Override
  public List<DailyRegistry> findAll() {
    return baseCrudService.findAll();
  }

  @Override
  public Page<DailyRegistry> findAll(Pageable pageable) {
    return baseCrudService.findAll(pageable);
  }

  @Override
  public DailyRegistry create(DailyRegistry entity) {
    throw new NotYetImplementedException();
  }

  @Override
  public DailyRegistry update(Long id, DailyRegistry updatedEntity) {
    throw new NotYetImplementedException();
  }

  @Override
  public DailyRegistry delete(Long id) {
    return baseCrudService.delete(id);
  }
}
