package com.kosinskyi.ecom.registry.entity.registry.daily.specification;

import com.kosinskyi.ecom.registry.entity.registry.daily.DailyRegistry;
import com.kosinskyi.ecom.registry.entity.registry.daily.DailyRegistry_;
import com.kosinskyi.ecom.registry.entity.specification.BaseSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class DailyRegistrySpecification implements BaseSpecification<DailyRegistry> {

  public Specification<DailyRegistry> yearEquals(Integer year) {
    return (root, query, builder) ->
        builder.equal(builder.function("YEAR", Integer.class, root.get(DailyRegistry_.registryDate)), year);
  }

  public Specification<DailyRegistry> monthEquals(Integer month) {
    return (root, query, builder) ->
        builder.equal(builder.function("MONTH", Integer.class, root.get(DailyRegistry_.registryDate)), month);
  }
}
