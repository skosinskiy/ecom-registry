package com.kosinskyi.ecom.registry.service;

import com.kosinskyi.ecom.registry.entity.base.BaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CrudService<E extends BaseEntity> {

  E findById(Long id);

  List<E> findAll();

  Page<E> findAll(Pageable pageable);

  E create(E entity);

  E update(Long id, E updatedEntity);

  E delete(Long id);

}
