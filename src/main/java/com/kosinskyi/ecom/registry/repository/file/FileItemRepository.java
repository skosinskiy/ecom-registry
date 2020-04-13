package com.kosinskyi.ecom.registry.repository.file;

import com.kosinskyi.ecom.registry.entity.file.FileItem;
import com.kosinskyi.ecom.registry.repository.base.JpaSpecificationExecutorRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileItemRepository extends JpaSpecificationExecutorRepository<FileItem, Long> {
}
