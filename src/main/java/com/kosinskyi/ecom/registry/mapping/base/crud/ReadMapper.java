package com.kosinskyi.ecom.registry.mapping.base.crud;

import com.kosinskyi.ecom.registry.entity.base.BaseEntity;
import com.kosinskyi.ecom.registry.mapping.base.BaseMapper;
import com.kosinskyi.ecom.registry.service.crud.ReadService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

public interface ReadMapper<E extends BaseEntity> extends BaseMapper {

  ReadService<E> readServiceSupplier();

  default <O> O findById(Long id, Class<O> responseClass) {
    return mapEntityToResponseDto(readServiceSupplier().findById(id), responseClass);
  }

  default <O> List<O> findAll(Class<O> responseClass) {
    return readServiceSupplier()
        .findAll()
        .stream()
        .map(e -> mapEntityToResponseDto(e, responseClass))
        .collect(Collectors.toList());
  }

  default <O> Page<O> findAll(Pageable pageable, Class<O> responseClass) {
    return readServiceSupplier().findAll(pageable).map(e -> mapEntityToResponseDto(e, responseClass));
  }

}
