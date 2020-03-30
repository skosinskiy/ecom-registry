package com.kosinskyi.ecom.registry.entity.file.listener;

import com.kosinskyi.ecom.registry.entity.file.FileItem;
import com.kosinskyi.ecom.registry.service.file.RegistryFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.persistence.PreRemove;

@Component
public class FileItemListener {

  private static RegistryFileService registryFileService;

  @Autowired
  public void setRegistryFileService(@Qualifier("localRegistryFileService") RegistryFileService registryFileService) {
    FileItemListener.registryFileService = registryFileService;
  }

  @PreRemove
  public void preRemove(FileItem fileItem) {
    registryFileService.removeFile(fileItem.getFileKey());
  }

}
