package com.kosinskyi.ecom.registry.mapping;

import com.kosinskyi.ecom.registry.dto.request.FileItemRequest;
import com.kosinskyi.ecom.registry.dto.response.FileItemResponse;
import com.kosinskyi.ecom.registry.entity.FileItem;
import com.kosinskyi.ecom.registry.service.FileItemService;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Component
public class FileItemMapper extends AbstractMapper<FileItem, FileItemRequest, FileItemResponse> {

  public FileItemResponse createFileItem(Date fileItemRequest, MultipartFile multipartFile) {
    FileItemService fileItemService = (FileItemService) crudService;
    FileItem fileItem = new FileItem();
    fileItem.setFileItemDate(fileItemRequest);
    return mapEntityToResponseDto(fileItemService.create(fileItem, multipartFile));
  }
}
