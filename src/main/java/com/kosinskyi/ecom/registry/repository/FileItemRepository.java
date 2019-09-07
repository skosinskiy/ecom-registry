package com.kosinskyi.ecom.registry.repository;

import com.kosinskyi.ecom.registry.entity.FileItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileItemRepository extends JpaRepository<FileItem, Long> {
}
