package com.kosinskyi.ecom.registry.service;

import com.kosinskyi.ecom.registry.entity.base.BaseEntity;
import com.kosinskyi.ecom.registry.exception.NoDataFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.lang.reflect.ParameterizedType;
import java.util.List;

public class BaseCrudService<E extends BaseEntity> implements CrudService<E> {

  private JpaRepository<E, Long> jpaRepository;

  public BaseCrudService(JpaRepository<E, Long> jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @SuppressWarnings("unchecked")
  private Class<E> entityClass = (Class<E>) ((ParameterizedType) getClass()
      .getGenericSuperclass()).getActualTypeArguments()[0];

  @Override
  public E findById(Long id) {
    return jpaRepository
        .findById(id)
        .orElseThrow(() -> new NoDataFoundException(
            String.format("No %s found by id %d", entityClass.getSimpleName(), id))
        );
  }

  @Override
  public List<E> findAll() {
    return jpaRepository.findAll();
  }

  @Override
  public Page<E> findAll(Pageable pageable) {
    return jpaRepository.findAll(pageable);
  }

  @Override
  public E create(E entity) {
    entity.setId(null);
    return jpaRepository.save(entity);
  }

  @Override
  public E update(Long id, E updatedEntity) {
    return null;
  }

  @Override
  public E delete(Long id) {
    E entity = findById(id);
    jpaRepository.delete(entity);
    return entity;
  }
}
