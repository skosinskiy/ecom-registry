package com.kosinskyi.ecom.registry.service.crud;

import com.kosinskyi.ecom.registry.entity.base.BaseEntity;
import com.kosinskyi.ecom.registry.error.exception.NoDataFoundException;
import com.kosinskyi.ecom.registry.service.crud.base.RepositorySupplier;

public interface DeleteService<E extends BaseEntity> extends RepositorySupplier<E> {

  default E delete(Long id) {
    E entity = repositorySupplier()
        .findById(id)
        .orElseThrow(() -> new NoDataFoundException(entityClassSupplier(), id));
    repositorySupplier().delete(entity);
    return entity;
  }

}
