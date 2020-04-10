package com.kosinskyi.ecom.registry.service.registry.daily.parsing;

import com.kosinskyi.ecom.registry.entity.registry.daily.DailyRegistryParseCriteria;
import com.kosinskyi.ecom.registry.repository.base.JpaSpecificationExecutorRepository;
import com.kosinskyi.ecom.registry.repository.registry.daily.DailyRegistryParseCriteriaRepository;
import com.kosinskyi.ecom.registry.service.crud.ReadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DailyRegistryParseCriteriaService implements ReadService<DailyRegistryParseCriteria> {

  private DailyRegistryParseCriteriaRepository repository;

  @Autowired
  public DailyRegistryParseCriteriaService(DailyRegistryParseCriteriaRepository repository) {
    this.repository = repository;
  }

  @Override
  public JpaSpecificationExecutorRepository<DailyRegistryParseCriteria, Long> repositorySupplier() {
    return repository;
  }

  @Override
  public Class<DailyRegistryParseCriteria> entityClassSupplier() {
    return DailyRegistryParseCriteria.class;
  }
}
