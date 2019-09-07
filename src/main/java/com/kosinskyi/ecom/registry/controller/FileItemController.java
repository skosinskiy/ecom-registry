package com.kosinskyi.ecom.registry.controller;

import com.kosinskyi.ecom.registry.dto.request.FileItemRequest;
import com.kosinskyi.ecom.registry.dto.response.FileItemResponse;
import com.kosinskyi.ecom.registry.mapping.FileItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Date;

@RestController
@RequestMapping("api/file-items")
public class FileItemController {

  private FileItemMapper fileItemMapper;

  @Autowired
  public FileItemController(FileItemMapper fileItemMapper) {
    this.fileItemMapper = fileItemMapper;
  }

  @PostMapping
  public ResponseEntity<FileItemResponse> createFileItem(
      @RequestParam Date fileItemRequest, @RequestParam MultipartFile multipartFile) {
    return ResponseEntity.ok(fileItemMapper.createFileItem(fileItemRequest, multipartFile));
  }

}
