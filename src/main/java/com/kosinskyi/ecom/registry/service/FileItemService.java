package com.kosinskyi.ecom.registry.service;

import com.kosinskyi.ecom.registry.entity.FileItem;
import com.kosinskyi.ecom.registry.repository.FileItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class FileItemService implements CrudService<FileItem> {

  private FileItemRepository fileItemRepository;
  private AmazonS3Service amazonS3Service;
  private UserService userService;

  @Autowired
  public FileItemService(FileItemRepository fileItemRepository, AmazonS3Service amazonS3Service, UserService userService) {
    this.fileItemRepository = fileItemRepository;
    this.amazonS3Service = amazonS3Service;
    this.userService = userService;
  }

  @Transactional
  public FileItem create(FileItem fileItem, MultipartFile multipartFile) {
    fileItem.setId(null);
    fileItem.setUser(userService.getCurrentUser());
    FileItem createdFileItem = fileItemRepository.save(fileItem);
    amazonS3Service.putRegistry(multipartFile, createdFileItem.getFileKey());
    return createdFileItem;
  }

  @Override
  public FileItem findById(Long id) {
    return null;
  }

  @Override
  public List<FileItem> findAll() {
    return null;
  }

  @Override
  public Page<FileItem> findAll(Pageable pageable) {
    return null;
  }

  @Override
  public FileItem create(FileItem entity) {
    return null;
  }

  @Override
  public FileItem update(Long id, FileItem updatedEntity) {
    return null;
  }

  @Override
  public FileItem delete(Long id) {
    return null;
  }
}
