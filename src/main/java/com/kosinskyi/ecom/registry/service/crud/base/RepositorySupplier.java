package com.kosinskyi.ecom.registry.service.crud.base;

import com.kosinskyi.ecom.registry.entity.base.BaseEntity;
import com.kosinskyi.ecom.registry.repository.base.JpaSpecificationExecutorRepository;

public interface RepositorySupplier<E extends BaseEntity> {

  JpaSpecificationExecutorRepository<E, Long> repositorySupplier();

  Class<E> entityClassSupplier();

}
