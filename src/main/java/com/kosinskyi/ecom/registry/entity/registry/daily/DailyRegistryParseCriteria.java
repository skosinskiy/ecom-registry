package com.kosinskyi.ecom.registry.entity.registry.daily;

import com.kosinskyi.ecom.registry.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.List;

@Entity
@Table(name = "daily_registry_parse_criteria")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DailyRegistryParseCriteria extends BaseEntity {

  @Column(name = "name", nullable = false, unique = true)
  private String name;

  @Column(name = "filter_column_name", nullable = false)
  private String filterColumnName;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(
      name = "daily_registry_parse_criteria_filter_value",
      joinColumns = @JoinColumn(name = "criteria_id"),
      uniqueConstraints = @UniqueConstraint(columnNames = {"criteria_id", "value"}))
  @Column(name = "value")
  private List<String> filterValues;
}
