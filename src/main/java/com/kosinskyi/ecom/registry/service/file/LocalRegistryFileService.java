package com.kosinskyi.ecom.registry.service.file;

import com.amazonaws.util.IOUtils;
import com.kosinskyi.ecom.registry.exception.ApplicationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Service
@Slf4j
public class LocalRegistryFileService implements RegistryFileService {

  private static final String STORAGE_PATH = "storage";

  private Path getPath(String fileKey) {
    return Paths.get(STORAGE_PATH, fileKey);
  }

  @Override
  public byte[] getBinaryFile(String fileKey) {
    String path = getPath(fileKey).toString();
    InputStream in = null;
    try {
      in = new FileInputStream(new File(path));
      byte[] result = IOUtils.toByteArray(in);
      in.close();
      return result;
    } catch (IOException exc) {
      log.error("Failed to read file {}: {}", path, exc.getMessage());
      throw new ApplicationException(exc.getMessage(), exc);
    } finally {
      if (Objects.nonNull(in)) {
        IOUtils.closeQuietly(in, null);
      }
    }
  }

  @Override
  public String uploadFile(MultipartFile multipartFile) {
    validateMimeType(multipartFile);
    String fileKey = generateFileKey(multipartFile);
    Path path = getPath(fileKey);
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
      Files.delete(getPath(fileKey));
    } catch (IOException exc) {
      log.error("Failed to remove file {}: {}", fileKey, exc.getMessage());
      throw new ApplicationException(exc.getMessage(), exc);
    }
  }
}
