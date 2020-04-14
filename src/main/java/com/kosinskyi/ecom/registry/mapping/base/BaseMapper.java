package com.kosinskyi.ecom.registry.mapping.base;

import com.kosinskyi.ecom.registry.dto.response.BaseEntityResponse;
import com.kosinskyi.ecom.registry.entity.base.BaseEntity;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

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

  default <O, E extends BaseEntity> List<O> mapEntityList(List<E> entityList, Class<O> responseClass) {
    return entityList.stream().map(e -> mapEntityToResponseDto(e, responseClass)).collect(Collectors.toList());
  }

  default <O, E extends BaseEntity> Page<O> mapEntityPage(Page<E> entityPage, Class<O> responseClass) {
    return entityPage.map(e -> mapEntityToResponseDto(e, responseClass));
  }
}
