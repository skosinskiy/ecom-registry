package com.kosinskyi.ecom.registry.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "file_manager_item")
@Data
@EqualsAndHashCode(callSuper = true)
public class FileItem extends BaseEntity {

  @Column(name = "file_key")
  private String fileKey;

  @Column(name = "file_item_date")
  private Date fileItemDate;

  @Column(name = "owner")
  private User user;

}
