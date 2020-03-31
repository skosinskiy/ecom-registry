package com.kosinskyi.ecom.registry.service.crud;

import com.kosinskyi.ecom.registry.entity.base.BaseEntity;
import com.kosinskyi.ecom.registry.error.exception.NoDataFoundException;
import com.kosinskyi.ecom.registry.service.crud.base.RepositorySupplier;
import com.kosinskyi.ecom.registry.utils.ObjectUtils;

public interface UpdateService<E extends BaseEntity> extends RepositorySupplier<E> {

  ObjectUtils objectUtils = new ObjectUtils();

  default E update(Long id, E updatedEntity) {
    E existingEntity = repositorySupplier()
        .findById(id)
        .orElseThrow(() -> new NoDataFoundException(entityClassSupplier(), id));
    objectUtils.copyNotNullProperties(updatedEntity, existingEntity);
    return repositorySupplier().save(existingEntity);
  }

}
