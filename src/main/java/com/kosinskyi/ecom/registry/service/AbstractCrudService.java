package com.kosinskyi.ecom.registry.service;

import com.kosinskyi.ecom.registry.entity.BaseEntity;
import com.kosinskyi.ecom.registry.entity.User;
import com.kosinskyi.ecom.registry.error.exception.NoDataFoundException;
import com.kosinskyi.ecom.registry.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public abstract class AbstractCrudService<E extends BaseEntity, R extends JpaRepository<E, Long>> {

  protected R jpaRepository;
  private ObjectUtils objectUtils;

  @Autowired
  public final void setObjectUtils(ObjectUtils objectUtils) {
    this.objectUtils = objectUtils;
  }

  public AbstractCrudService(R jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @SuppressWarnings("unchecked")
  private Class<E> entityClass =
      (Class<E>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

  public E findById(Long id) {
    return jpaRepository
        .findById(id)
        .orElseThrow(() ->
            new NoDataFoundException(String.format("No %s found by id %s", entityClass.getSimpleName(), id)));
  }

  public <P> E findByParam(P param, Function<P, Optional<E>> function) {
    return function
        .apply(param)
        .orElseThrow(() ->
            new NoDataFoundException(String.format("No %s found by param %s", entityClass.getSimpleName(), param)));
  }

  public Optional<E> findOptionalById(Long id) {
    return jpaRepository.findById(id);
  }

  public List<E> findAll() {
    return jpaRepository.findAll();
  }

  public Page<E> findAll(Pageable pageable) {
    return jpaRepository.findAll(pageable);
  }

  public E create(E entity) {
    entity.setId(null);
    return jpaRepository.save(entity);
  }

  public E update(Long id, E updatedEntity) {
    E existingEntity = findById(id);
    objectUtils.copyNotNullProperties(updatedEntity, existingEntity);
    return jpaRepository.save(existingEntity);
  }

  public E delete(Long id) {
    E existingEntity = findById(id);
    jpaRepository.delete(existingEntity);
    return existingEntity;
  }

}
