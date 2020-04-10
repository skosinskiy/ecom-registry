package com.kosinskyi.ecom.registry.repository.registry.daily;

import com.kosinskyi.ecom.registry.entity.registry.daily.DailyRegistryParseCriteria;
import com.kosinskyi.ecom.registry.repository.base.JpaSpecificationExecutorRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyRegistryParseCriteriaRepository extends
    JpaSpecificationExecutorRepository<DailyRegistryParseCriteria, Long> {
}
