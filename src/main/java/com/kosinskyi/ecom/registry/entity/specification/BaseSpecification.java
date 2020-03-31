package com.kosinskyi.ecom.registry.entity.specification;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.metamodel.SingularAttribute;

public interface BaseSpecification<E> {

  default <T> Specification<E> entityFieldEquals(SingularAttribute<E, T> singularAttribute, T value) {
    return (root, criteriaQuery, cb) -> cb.equal(root.get(singularAttribute), value);
  }

}
