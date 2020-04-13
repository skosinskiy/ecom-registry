package com.kosinskyi.ecom.registry.service.file.storage;

import com.kosinskyi.ecom.registry.entity.file.FileItem;
import com.kosinskyi.ecom.registry.entity.file.constants.Extension;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.UUID;

public interface StorageFileService {

  default String generateFileKey() {
    return UUID.randomUUID().toString();
  }

  byte[] getBinaryFile(FileItem fileItem);

  String uploadFile(MultipartFile multipartFile, Extension extension);

  void removeFile(FileItem fileItem);

  String saveZip(Map<String, byte[]> fileNameBytesMap);

}
