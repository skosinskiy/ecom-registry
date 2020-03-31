package com.kosinskyi.ecom.registry.service.file;

import com.amazonaws.services.s3.AmazonS3Client;
import com.kosinskyi.ecom.registry.error.exception.ApplicationException;
import com.kosinskyi.ecom.registry.error.exception.NotYetImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class AmazonS3RegistryFileService implements RegistryFileService {

  private static final String AMAZON_S3_BUCKET = "ecom-registry";

  private AmazonS3Client s3Client;

  @Autowired
  public AmazonS3RegistryFileService(AmazonS3Client s3Client) {
    this.s3Client = s3Client;
  }

  @Override
  public byte[] getBinaryFile(String fileKey) {
    throw new NotYetImplementedException();
  }

  @Override
  public String uploadFile(MultipartFile multipartFile) {
    try {
      String fileKey = generateFileKey(multipartFile);
      s3Client.putObject(AMAZON_S3_BUCKET, fileKey, multipartFile.getInputStream(), null);
      return fileKey;
    } catch (IOException exc) {
      throw new ApplicationException(exc.getMessage(), exc);
    }
  }

  @Override
  public void removeFile(String fileKey) {
    throw new NotYetImplementedException();
  }
}
