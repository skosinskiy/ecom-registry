package com.kosinskyi.ecom.registry.mapping.base;

import com.kosinskyi.ecom.registry.entity.base.BaseEntity;
import org.modelmapper.ModelMapper;

public interface BaseMapper {

  ModelMapper modelMapperSupplier();

  default <I, E extends BaseEntity> E mapRequestDtoToEntity(I requestDto, Class<E> entityClass) {
    return requestDto != null
        ? modelMapperSupplier().map(requestDto, entityClass)
        : null;
  }

  default <O, E extends BaseEntity> O mapEntityToResponseDto(E entity, Class<O> responseClass) {
    return entity != null
        ? modelMapperSupplier().map(entity, responseClass)
        : null;
  }

}
