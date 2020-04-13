package com.kosinskyi.ecom.registry.controller;

import com.kosinskyi.ecom.registry.service.file.FileItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/files")
public class FileItemController {

  private FileItemService fileItemService;

  @Autowired
  public FileItemController(FileItemService fileItemService) {
    this.fileItemService = fileItemService;
  }

  @GetMapping(value = "binary/{fileItemId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
  public byte[] getBinary(@PathVariable Long fileItemId) {
    return fileItemService.getBinary(fileItemId);
  }

}
