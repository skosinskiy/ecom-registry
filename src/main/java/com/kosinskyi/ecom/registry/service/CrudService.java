package com.kosinskyi.ecom.registry.service;

import com.kosinskyi.ecom.registry.entity.BaseEntity;

import java.util.List;

public interface CrudService<E extends BaseEntity> {

  E getById(Long id);

  List<E> getAll();

  E create(E entity);

  E update(Long id, E entity);

  E delete(Long id);

}
