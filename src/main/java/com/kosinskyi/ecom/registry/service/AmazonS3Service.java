package com.kosinskyi.ecom.registry.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.kosinskyi.ecom.registry.exception.ActionForbiddenException;
import com.kosinskyi.ecom.registry.exception.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class AmazonS3Service {

  private static final String AMAZON_S3_BUCKET = "ecom-registry";
  private static final Map<String, String> allowedMimeTypes = new HashMap<>();
  private AmazonS3Client s3Client;

  @Autowired
  public AmazonS3Service(AmazonS3Client s3Client) {
    this.s3Client = s3Client;
  }

  @PostConstruct
  private void init() {
    allowedMimeTypes.put("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", ".xlsx");
    allowedMimeTypes.put("application/vnd.ms-excel", ".xls");
  }

  public void putRegistry(MultipartFile multipartFile, String fileKey) {
    String contentType = multipartFile.getContentType();
    if (!allowedMimeTypes.containsKey(contentType)) {
      throw new ActionForbiddenException(String.format("Only %s formats allowed", allowedMimeTypes));
    }
    try {
      s3Client.putObject(AMAZON_S3_BUCKET, fileKey, multipartFile.getInputStream(), null);
    } catch (IOException exc) {
      throw new ApplicationException(exc.getMessage(), exc);
    }
  }

  public String generateS3FileKey(String contentType) {
    return UUID.randomUUID().toString() + allowedMimeTypes.get(contentType);
  }
}
