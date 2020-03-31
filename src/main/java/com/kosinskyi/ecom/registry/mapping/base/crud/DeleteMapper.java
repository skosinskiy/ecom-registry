package com.kosinskyi.ecom.registry.mapping.base.crud;

import com.kosinskyi.ecom.registry.entity.base.BaseEntity;
import com.kosinskyi.ecom.registry.mapping.base.BaseMapper;
import com.kosinskyi.ecom.registry.service.crud.DeleteService;

public interface DeleteMapper<E extends BaseEntity> extends BaseMapper {

  DeleteService<E> deleteServiceSupplier();

  default <O> O delete(Long id, Class<O> responseClass) {
    return mapEntityToResponseDto(deleteServiceSupplier().delete(id), responseClass);
  }

}
