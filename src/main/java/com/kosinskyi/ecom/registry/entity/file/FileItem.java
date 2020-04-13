package com.kosinskyi.ecom.registry.entity.file;

import com.kosinskyi.ecom.registry.entity.base.BaseEntity;
import com.kosinskyi.ecom.registry.entity.file.constants.Extension;
import com.kosinskyi.ecom.registry.entity.file.listener.FileItemListener;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = "file_item")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@EntityListeners(FileItemListener.class)
public class FileItem extends BaseEntity {

  @Column(name = "key", nullable = false, unique = true)
  private String key;

  @Column(name = "extension", nullable = false)
  @Enumerated(EnumType.STRING)
  private Extension extension;

  @Column(name = "size")
  private Long size;

}
