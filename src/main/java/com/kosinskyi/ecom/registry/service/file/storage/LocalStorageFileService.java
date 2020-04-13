package com.kosinskyi.ecom.registry.service.file.storage;

import com.amazonaws.util.IOUtils;
import com.kosinskyi.ecom.registry.entity.file.FileItem;
import com.kosinskyi.ecom.registry.entity.file.constants.Extension;
import com.kosinskyi.ecom.registry.error.exception.ApplicationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@Slf4j
public class LocalStorageFileService implements StorageFileService {

  private static final String STORAGE_PATH = "storage";

  private Path getPath(FileItem fileItem) {
    String fileName = String.format("%s.%s", fileItem.getKey(), fileItem.getExtension().getValue());
    return Paths.get(STORAGE_PATH, fileName);
  }

  private Path getPath(String key, Extension extension) {
    String fileName = String.format("%s.%s", key, extension.getValue());
    return Paths.get(STORAGE_PATH, fileName);
  }

  @Override
  public byte[] getBinaryFile(FileItem fileItem) {
    String path = getPath(fileItem).toString();
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
  public String uploadFile(MultipartFile multipartFile, Extension extension) {
    String fileKey = generateFileKey();
    Path path = getPath(fileKey, extension);
    try {
      multipartFile.transferTo(path);
      return fileKey;
    } catch (IOException exc) {
      log.error("Failed to save file {}: {}", path, exc.getMessage());
      throw new ApplicationException(exc.getMessage(), exc);
    }
  }

  @Override
  public void removeFile(FileItem fileItem) {
    try {
      Files.delete(getPath(fileItem));
    } catch (IOException exc) {
      log.error("Failed to remove file {}: {}", fileItem.getKey(), exc.getMessage());
      throw new ApplicationException(exc.getMessage(), exc);
    }
  }

  @Override
  public String saveZip(Map<String, byte[]> map) {
    try {
      String zipName = generateFileKey();
      ZipOutputStream zos = new ZipOutputStream(
          new BufferedOutputStream(
              new FileOutputStream(getPath(zipName, Extension.ZIP).toString())));
      map.forEach((key, value) -> addFileToZip(zos, key, value));
      zos.close();
      return zipName;
    } catch (IOException exc) {
      log.error(exc.getMessage(), exc);
      throw new ApplicationException(exc.getMessage(), exc);
    }
  }

  private void addFileToZip(ZipOutputStream zos, String name, byte[] bytes) {
    try {
      zos.putNextEntry(new ZipEntry(name));
      zos.write(bytes);
      zos.closeEntry();
    } catch (IOException exc) {
      log.error(exc.getMessage());
      throw new ApplicationException(exc.getMessage(), exc);
    }
  }
}
