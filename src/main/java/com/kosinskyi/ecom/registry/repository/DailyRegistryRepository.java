package com.kosinskyi.ecom.registry.repository;

import com.kosinskyi.ecom.registry.entity.file.FileItem;
import com.kosinskyi.ecom.registry.entity.registry.DailyRegistry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyRegistryRepository extends JpaRepository<DailyRegistry, Long> {
}
