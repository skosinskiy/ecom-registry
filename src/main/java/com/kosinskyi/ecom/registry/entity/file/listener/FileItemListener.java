package com.kosinskyi.ecom.registry.entity.file.listener;

import com.kosinskyi.ecom.registry.entity.file.FileItem;
import com.kosinskyi.ecom.registry.service.file.storage.StorageFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.persistence.PreRemove;

@Component
public class FileItemListener {

  private static StorageFileService storageFileService;

  @Autowired
  public void setRegistryFileService(@Qualifier("localStorageFileService") StorageFileService storageFileService) {
    FileItemListener.storageFileService = storageFileService;
  }

  @PreRemove
  public void preRemove(FileItem fileItem) {
    storageFileService.removeFile(fileItem);
  }

}
