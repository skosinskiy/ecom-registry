package com.kosinskyi.ecom.registry.entity.registry.daily;

import com.kosinskyi.ecom.registry.entity.base.BaseEntity;
import com.kosinskyi.ecom.registry.entity.file.FileItem;
import com.kosinskyi.ecom.registry.entity.registry.daily.constants.DailyRegistryStatus;
import com.kosinskyi.ecom.registry.entity.user.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "daily_registry")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DailyRegistry extends BaseEntity {

  @Column(name = "registry_date", unique = true)
  private LocalDate registryDate;

  @Column(name = "status", nullable = false)
  @Enumerated(EnumType.STRING)
  private DailyRegistryStatus status;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "registry_item_id", nullable = false)
  private FileItem registryItem;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "parsed_registry_item_id")
  private FileItem parsedRegistryItem;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

}
