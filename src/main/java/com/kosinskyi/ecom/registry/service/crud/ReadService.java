package com.kosinskyi.ecom.registry.service.crud;

import com.kosinskyi.ecom.registry.entity.base.BaseEntity;
import com.kosinskyi.ecom.registry.error.exception.NoDataFoundException;
import com.kosinskyi.ecom.registry.service.crud.base.RepositorySupplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public interface ReadService<E extends BaseEntity> extends RepositorySupplier<E> {

  default E findById(Long id) {
    return repositorySupplier()
        .findById(id)
        .orElseThrow(() -> new NoDataFoundException(entityClassSupplier(), id));
  }

  default E findOne(Specification<E> specification) {
    return repositorySupplier()
        .findOne(specification)
        .orElseThrow(() -> new NoDataFoundException(
            String.format("No %s found by query", entityClassSupplier().getSimpleName())));
  }

  default Optional<E> findOptionalById(Long id) {
    return repositorySupplier().findById(id);
  }

  default Optional<E> findOptionalOne(Specification<E> specification) {
    return repositorySupplier().findOne(specification);
  }

  default List<E> findAll() {
    return repositorySupplier().findAll();
  }

  default List<E> findAll(Specification<E> specification) {
    return repositorySupplier().findAll(specification);
  }

  default Page<E> findAll(Pageable pageable) {
    return repositorySupplier().findAll(pageable);
  }

  default Page<E> findAll(Specification<E> specification, Pageable pageable) {
    return repositorySupplier().findAll(specification, pageable);
  }

}
