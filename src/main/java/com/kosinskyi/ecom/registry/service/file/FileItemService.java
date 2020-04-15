package com.kosinskyi.ecom.registry.service.file;

import com.kosinskyi.ecom.registry.entity.file.FileItem;
import com.kosinskyi.ecom.registry.entity.file.FileItem_;
import com.kosinskyi.ecom.registry.entity.file.constants.Extension;
import com.kosinskyi.ecom.registry.entity.file.specification.FileItemSpecification;
import com.kosinskyi.ecom.registry.error.exception.ActionForbiddenException;
import com.kosinskyi.ecom.registry.repository.base.JpaSpecificationExecutorRepository;
import com.kosinskyi.ecom.registry.repository.file.FileItemRepository;
import com.kosinskyi.ecom.registry.service.crud.ReadService;
import com.kosinskyi.ecom.registry.service.file.storage.StorageFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class FileItemService implements ReadService<FileItem> {

  private static final Map<String, Extension> ALLOWED_MIME_TYPES = Collections.unmodifiableMap(getAllowedMimeTypes());

  private static Map<String, Extension> getAllowedMimeTypes() {
    Map<String, Extension> allowedMimeTypes = new HashMap<>();
    allowedMimeTypes.put("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", Extension.XLSX);
    allowedMimeTypes.put("application/vnd.ms-excel", Extension.XLS);
    return allowedMimeTypes;
  }

  private FileItemRepository repository;
  private FileItemSpecification specification;
  private StorageFileService storageFileService;

  @Autowired
  public FileItemService(
      FileItemRepository repository,
      FileItemSpecification specification,
      @Qualifier("localStorageFileService") StorageFileService storageFileService) {
    this.repository = repository;
    this.specification = specification;
    this.storageFileService = storageFileService;
  }

  @Override
  public JpaSpecificationExecutorRepository<FileItem, Long> repositorySupplier() {
    return repository;
  }

  @Override
  public Class<FileItem> entityClassSupplier() {
    return FileItem.class;
  }

  private Extension getExtension(MultipartFile multipartFile) {
    String contentType = multipartFile.getContentType();
    if (!ALLOWED_MIME_TYPES.containsKey(contentType)) {
      throw new ActionForbiddenException(String.format("Only %s formats allowed", ALLOWED_MIME_TYPES));
    }
    return ALLOWED_MIME_TYPES.get(contentType);
  }

  private FileItem getFileItem(String fileKey, Extension extension) {
    FileItem fileItem = new FileItem();
    fileItem.setId(null);
    fileItem.setExtension(extension);
    fileItem.setKey(fileKey);
    return fileItem;
  }

  public byte[] getBinary(Long id) {
    return storageFileService.getBinaryFile(findById(id));
  }

  public byte[] getBinary(FileItem fileItem) {
    Long id = fileItem.getId();
    log.info("Getting binary file with id={}", id);
    byte[] binaryFile = storageFileService.getBinaryFile(fileItem);
    log.info("Binary file with id={} downloaded", id);
    return binaryFile;
  }

  public FileItem uploadFile(MultipartFile multipartFile) {
    Extension extension = getExtension(multipartFile);
    String key = storageFileService.uploadFile(multipartFile, extension);
    FileItem fileItem = new FileItem();
    fileItem.setKey(key);
    fileItem.setExtension(extension);
    fileItem.setSize(multipartFile.getSize());
    return fileItem;
  }

  public FileItem findByKey(String key) {
    return findOne(specification.entityFieldEquals(FileItem_.key, key));
  }

  public FileItem saveZip(Map<String, byte[]> fileNameBytesMap) {
    log.info("Saving zip");
    FileItem fileItem = getFileItem(storageFileService.saveZip(fileNameBytesMap), Extension.ZIP);
    log.info("Zip saved successfully");
    return fileItem;
  }
}
