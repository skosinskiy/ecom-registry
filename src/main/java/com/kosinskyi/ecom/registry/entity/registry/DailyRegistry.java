package com.kosinskyi.ecom.registry.entity.registry;

import com.kosinskyi.ecom.registry.entity.base.BaseEntity;
import com.kosinskyi.ecom.registry.entity.file.FileItem;
import com.kosinskyi.ecom.registry.entity.user.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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

  @Column(name = "registry_date")
  private LocalDate registryDate;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "file_item_id")
  private FileItem fileItem;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

}
