package com.kosinskyi.ecom.registry.repository.base;

import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository;
import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface JpaSpecificationExecutorRepository<E, I extends Serializable> extends
    EntityGraphJpaRepository<E, I>, EntityGraphJpaSpecificationExecutor<E> {
}
