package com.kosinskyi.ecom.registry.service.file;

import com.kosinskyi.ecom.registry.exception.ApplicationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Slf4j
public class LocalRegistryFileService implements RegistryFileService {

  private static final String STORAGE_PATH = "storage";

  @Override
  public String uploadFile(MultipartFile multipartFile) {
    validateMimeType(multipartFile);
    String fileKey = generateFileKey(multipartFile);
    Path path = Paths.get(STORAGE_PATH, fileKey);
    try {
      multipartFile.transferTo(path);
    } catch (IOException exc) {
      log.error("Failed to save file {}: {}", path, exc.getMessage());
      throw new ApplicationException(exc.getMessage(), exc);
    }
    return fileKey;
  }

  @Override
  public void removeFile(String fileKey) {
    try {
      Files.delete(Paths.get(STORAGE_PATH, fileKey));
    } catch (IOException exc) {
      log.error("Failed to remove file {}: {}", fileKey, exc.getMessage());
      throw new ApplicationException(exc.getMessage(), exc);
    }
  }
}
