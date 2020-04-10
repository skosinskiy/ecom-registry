package com.kosinskyi.ecom.registry.repository.registry.daily;

import com.kosinskyi.ecom.registry.entity.registry.daily.DailyRegistry;
import com.kosinskyi.ecom.registry.repository.base.JpaSpecificationExecutorRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyRegistryRepository extends JpaSpecificationExecutorRepository<DailyRegistry, Long> {

}
