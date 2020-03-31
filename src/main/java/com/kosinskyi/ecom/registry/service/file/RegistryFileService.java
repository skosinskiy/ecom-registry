package com.kosinskyi.ecom.registry.service.file;

import com.kosinskyi.ecom.registry.exception.ActionForbiddenException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public interface RegistryFileService {

  Map<String, String> ALLOWED_MIME_TYPES = Collections.unmodifiableMap(getAllowedMimeTypes());

  static Map<String, String> getAllowedMimeTypes() {
    Map<String, String> allowedMimeTypes = new HashMap<>();
    allowedMimeTypes.put("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", ".xlsx");
    allowedMimeTypes.put("application/vnd.ms-excel", ".xls");
    return allowedMimeTypes;
  }

  default void validateMimeType(MultipartFile multipartFile) {
    String contentType = multipartFile.getContentType();
    if (!ALLOWED_MIME_TYPES.containsKey(contentType)) {
      throw new ActionForbiddenException(String.format("Only %s formats allowed", ALLOWED_MIME_TYPES));
    }
  }

  default String generateFileKey(MultipartFile multipartFile) {
    return UUID.randomUUID().toString() + ALLOWED_MIME_TYPES.get(multipartFile.getContentType());
  }

  byte[] getBinaryFile(String fileKey);

  String uploadFile(MultipartFile multipartFile);

  void removeFile(String fileKey);

}
