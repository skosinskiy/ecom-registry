package com.kosinskyi.ecom.registry.repository.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface JpaSpecificationExecutorRepository<E, I> extends JpaSpecificationExecutor<E>, JpaRepository<E, I> {
}
