package com.kosinskyi.ecom.registry.service.file.storage;

import com.amazonaws.services.s3.AmazonS3Client;
import com.kosinskyi.ecom.registry.entity.file.FileItem;
import com.kosinskyi.ecom.registry.entity.file.constants.Extension;
import com.kosinskyi.ecom.registry.error.exception.NotYetImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
public class AmazonS3StorageFileService implements StorageFileService {

  private static final String AMAZON_S3_BUCKET = "ecom-registry";

  private AmazonS3Client s3Client;

  @Autowired
  public AmazonS3StorageFileService(AmazonS3Client s3Client) {
    this.s3Client = s3Client;
  }

  @Override
  public byte[] getBinaryFile(FileItem fileItem) {
    throw new NotYetImplementedException();
  }

  @Override
  public String uploadFile(MultipartFile multipartFile, Extension extension) {
    throw new NotYetImplementedException();
  }

  @Override
  public void removeFile(FileItem fileItem) {
    throw new NotYetImplementedException();
  }

  @Override
  public String saveZip(Map<String, byte[]> fileNameBytesMap) {
    throw new NotYetImplementedException();
  }
}
