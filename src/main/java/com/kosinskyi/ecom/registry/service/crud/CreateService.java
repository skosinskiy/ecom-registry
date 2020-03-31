package com.kosinskyi.ecom.registry.service.crud;

import com.kosinskyi.ecom.registry.entity.base.BaseEntity;
import com.kosinskyi.ecom.registry.service.crud.base.RepositorySupplier;

public interface CreateService<E extends BaseEntity> extends RepositorySupplier<E> {

  default E create(E entity) {
    entity.setId(null);
    return repositorySupplier().save(entity);
  }

}
